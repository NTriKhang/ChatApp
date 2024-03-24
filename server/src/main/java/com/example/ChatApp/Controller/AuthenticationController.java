package com.example.ChatApp.Controller;

import com.example.ChatApp.Models.Users;
import com.example.ChatApp.Services.AuthenticationService;
import com.example.ChatApp.Services.JWTService;
import com.example.ChatApp.Services.UserService;
import com.example.ChatApp.dto.IdDto;
import com.example.ChatApp.dto.SignInDto;
import com.example.ChatApp.dto.SignUpDto;
/*import lombok.RequiredArgsConstructor;*/

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import com.example.ChatApp.dto.UserUpdateDto;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	@Autowired
	private UserService userService;
	@Autowired
	private JWTService jwtService;

	public static String upLoadDirectory=System.getProperty("user.dir")+"/src/main/resources/UserImage";
	@PostMapping("/signup")
	public ResponseEntity<Users> signup(@RequestBody SignUpDto sigUpRequest) {
		Users users = userService.signup(sigUpRequest);

		if (users.Tag == "not unique") {
			return new ResponseEntity<Users>(users, HttpStatus.CONFLICT);
		} else if (users.Email == "not unique") {
			return new ResponseEntity<Users>(users, HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Users>(users, HttpStatus.OK);

		// return new ResponseEntity<SignUpDto>(sigUpRequest, HttpStatus.OK);
	}

	/// Nhan 2 tham so, account_name (có thể là email hoặc tag), password
	@PostMapping("/signin")
	public ResponseEntity<Users> signin(@RequestBody SignInDto signInDto) {
		System.out.println(signInDto.Account_name + ' ' + signInDto.Password);
		Optional<Users> user = userService.signin(signInDto);
		if (user.isEmpty())
			return new ResponseEntity<>(user.get(), HttpStatus.CONFLICT);
		HttpHeaders headers = new HttpHeaders();
		System.out.println(user.get()._id);
		// String token = jwtService.generateToken(userid.get()._id);
		// System.out.println(token);
		headers.add(HttpHeaders.SET_COOKIE, "userId=" + user.get()._id + "; HttpOnly; Path=/");
		return ResponseEntity.ok().headers(headers).body(user.get());
	}
	@PostMapping("/update")
	public ResponseEntity<?> updateUser(@RequestBody UserUpdateDto userUpdateRequest) {
		try {
			UpdateResult user = userService.updateUser(userUpdateRequest);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (RuntimeException ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/Upload_UserImages/{UserID}")
	public ResponseEntity<?> UploadIMG(@PathVariable String UserID, @RequestParam MultipartFile file)
			throws IOException {
		System.out.println(UserID);
		String uploadImage = UserService.uploadImageUser(UserID, file);
		if (uploadImage != null)
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

	}
}
