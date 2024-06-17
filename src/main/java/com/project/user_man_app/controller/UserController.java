package com.project.user_man_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.user_man_app.exceptions.UserNotFoundException;
import com.project.user_man_app.model.User;
import com.project.user_man_app.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {
	private  UserService userService;
	
	UserController(UserService userService){
		this.userService=userService;
	}
	@Operation(summary = "Register a new user")
	@PostMapping("/register")
	public ResponseEntity<String> addUser(@Valid @RequestBody User user,BindingResult bindingResults){
		if(bindingResults.hasErrors()) {
			return ResponseEntity.badRequest().body(buildValidationErrorsResponse(bindingResults));
		}
		userService.registerUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully");
	}
	@Operation(summary="Fetch user details by username")
	@GetMapping("/fetch")
	public ResponseEntity<User> fetchUser(@RequestParam String username) throws UserNotFoundException{
		User user=userService.fetchUserByUsername(username);
		return ResponseEntity.ok(user);
	}
	
	private String buildValidationErrorsResponse(BindingResult bindingResult) {
        StringBuilder sb = new StringBuilder();
        bindingResult.getAllErrors().forEach(error -> sb.append(error.getDefaultMessage()).append("\n"));
        return sb.toString();
    }
}
