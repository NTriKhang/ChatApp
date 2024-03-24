package com.example.ChatApp.Services;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import com.example.ChatApp.dto.UserUpdateDto;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
/*import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;*/
/*import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;*/
import org.springframework.stereotype.Service;

import com.example.ChatApp.Models.Users;
import com.example.ChatApp.Repositories.UsersRepository;
import com.example.ChatApp.dto.IdDto;
import com.example.ChatApp.dto.SignInDto;
import com.example.ChatApp.dto.SignUpDto;

import javax.xml.datatype.DatatypeConstants;

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
		return usersRepository.save(users);
	}

	public Optional<Users> signin(SignInDto signInDto) {
		System.out.println(signInDto.Account_name);
		Optional<Users> user_id = usersRepository.authLogin(signInDto.Account_name, signInDto.Password);
		return user_id;
	}
	public Users updateUser(UserUpdateDto userUpdateRequest) {
		ObjectId id = new ObjectId();
		Users users = new Users();
		// Fetch the user from db with userService
		UsersRepository userRepository = null;
		Optional<Users> user = userRepository.find_ById(userUpdateRequest.Id);

		// Condition for editted_day > 60

		Date editedDay = users.Edited_day;
		Date currentDate = new Date();
		Date createDay=users.Created_day;
		long millisBetween = currentDate.getTime() - editedDay.getTime();
		long daysBetween = millisBetween / (24 * 60 * 60 * 1000);
		long millisBetween1 = currentDate.getTime() - createDay.getTime();
		long daysBetween1 = millisBetween / (24 * 60 * 60 * 1000);
		if (daysBetween <= 60||daysBetween1<=60) {
			throw new RuntimeException("You can only update your profile every 60 days");
		}

		Optional<Users> userWithSameEmail = userRepository.findByEmail(userUpdateRequest.getEmail());
		if (!usersRepository.findByEmail(userUpdateRequest.Email).isEmpty()) {
			throw new RuntimeException("Email is already in use");
		}
		users.Display_name=userUpdateRequest.DisplayName;
		users.Email=userUpdateRequest.Email;
		users.Tag= userUpdateRequest.Tag;
		users.Image_path= userUpdateRequest.ImagePath;
		users.Birth=userUpdateRequest.Birth;
		users.Background_image_path= userUpdateRequest.BackgroundImagePath;
		users.Edited_day= new Date();
		return usersRepository.save(users);
	}
}
