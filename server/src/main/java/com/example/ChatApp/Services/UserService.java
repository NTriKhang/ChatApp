package com.example.ChatApp.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import com.example.ChatApp.Config.Utility;
import com.example.ChatApp.dto.UserUpdateDto;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.ChatApp.Models.Users;
import com.example.ChatApp.Models.Submodels.MessageGroup_User;
import com.example.ChatApp.Repositories.UsersRepository;

import com.example.ChatApp.dto.SignInDto;
import com.example.ChatApp.dto.SignUpDto;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.mongodb.core.query.Query;


@Service
public class UserService {
	@Autowired
	private UsersRepository usersRepository;
	/*
	 * @Autowired private PasswordEncoder passwordEncoder;
	 */

	/*
	 * public Optional<Users> getUserByTag(String Tag){ return
	 * usersRepository.findByTag(Tag);
	 * 
	 * }
	 */
	@Autowired
	private MongoTemplate mongoTemplate;
	public static String upLoadDirectory=System.getProperty("user.dir")+"\\src\\main\\resources\\static\\";
	
	public Users signup(SignUpDto signUpRequest) {

		Users users = new Users();
		if (!usersRepository.findTag(signUpRequest.Tag).isEmpty()) {
			users.Tag = "not unique";
			return users;
		}

		if (!usersRepository.findByEmail(signUpRequest.Email).isEmpty()) {
			users.Email = "not unique";
			return users;
		}

		users.Email = signUpRequest.Email;
		users.Display_name = signUpRequest.Display_Name;
		users.Password = signUpRequest.Password;
		users.Birth = signUpRequest.Birth;
		users.Created_day = new Date();
		users.Tag = signUpRequest.Tag;
		users.Background_image_path = "";
		users.Image_path = "";
		users.List_message_group = new ArrayList<MessageGroup_User>();
		return usersRepository.save(users);
	}

	public Optional<Users> signin(SignInDto signInDto) {
		System.out.println(signInDto.Account_name);
		Optional<Users> user = usersRepository.authLogin(signInDto.Account_name, signInDto.Password);
		return user;
	}
	public UpdateResult updateUser(UserUpdateDto userUpdateRequest) {
		ObjectId id = new ObjectId(userUpdateRequest.Id);
		Query query = new Query(Criteria.where("_id").is(id));

		Date editedDay = mongoTemplate.findOne(query, Users.class).Edited_day;
		Date createDay = mongoTemplate.findOne(query, Users.class).Created_day;
		long millisBetween = new Date().getTime() - editedDay.getTime();
		long daysBetween = millisBetween / (24 * 60 * 60 * 1000);
		long millisBetween1 = new Date().getTime() - createDay.getTime();
		long daysBetween1 = millisBetween / (24 * 60 * 60 * 1000);
		
		System.out.println(millisBetween + " " + daysBetween);

		if (editedDay==null&&daysBetween<=60) {
			return null;
		}
		if (daysBetween1<=60) {
			return null;
		}

		if (usersRepository.findByEmailExceptId(userUpdateRequest.Email, userUpdateRequest.Id).isPresent()) {
			return null;
		}

		Update update = new Update()
				.set("Display_name", userUpdateRequest.getDisplayName())
				.set("Email", userUpdateRequest.getEmail())
				.set("Tag", userUpdateRequest.getTag())
				.set("Birth", userUpdateRequest.getBirth())
				.set("Edited_day", new Date());

		return mongoTemplate.updateFirst(query, update, Users.class);
	}
	public String uploadUserProfileImage(String userId, MultipartFile file) throws IOException {
		ObjectId id = new ObjectId(userId);
		String folderPath = upLoadDirectory + Utility.FilePath.UserImagePath;
		String fileName = UUID.randomUUID() + file.getOriginalFilename();
		String filePath = folderPath + fileName;
		Optional<Users> user = usersRepository.findById(id);

		if (!user.isPresent()) {
			return null;
		}

		Query query = new Query(Criteria.where("_id").is(id));
		Update update = new Update().set("Image_path", fileName);
		UpdateResult result = mongoTemplate.updateFirst(query, update, Users.class);

		if (result.wasAcknowledged()) {
			Files.write(Paths.get(filePath), file.getBytes());
			return Utility.FilePath.UserImagePath + fileName;
		} else {
			return null;
		}
	}

	public String uploadUserBackgroundImage(String userId, MultipartFile file) throws IOException {
		ObjectId id = new ObjectId(userId);
		String folderPath = upLoadDirectory + Utility.FilePath.UserImagePath;
		String fileName = UUID.randomUUID() + file.getOriginalFilename();
		String filePath = folderPath + fileName;
		Optional<Users> user = usersRepository.findById(id);

		if (!user.isPresent()) {
			return null;
		}

		Query query = new Query(Criteria.where("_id").is(id));
		Update update = new Update().set("Background_image_path", fileName);
		UpdateResult result = mongoTemplate.updateFirst(query, update, Users.class);

		if (result.wasAcknowledged()) {
			Files.write(Paths.get(filePath), file.getBytes());
			return Utility.FilePath.UserImagePath + fileName;
		} else {
			return null;
		}
	}
}
