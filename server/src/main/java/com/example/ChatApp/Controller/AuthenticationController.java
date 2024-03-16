package com.example.ChatApp.Controller;

import com.example.ChatApp.Models.Users;
import com.example.ChatApp.Services.AuthenticationService;
import com.example.ChatApp.Services.JWTService;
import com.example.ChatApp.Services.UserService;
import com.example.ChatApp.dto.IdDto;
import com.example.ChatApp.dto.SignInDto;
import com.example.ChatApp.dto.SignUpDto;
/*import lombok.RequiredArgsConstructor;*/

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.NotFound;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	@Autowired
	private UserService userService;
	@Autowired
	private JWTService jwtService;

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
}
