package com.project.user_man_app.service;

import org.springframework.stereotype.Service;

import com.project.user_man_app.exceptions.UserNotFoundException;
import com.project.user_man_app.model.User;
import com.project.user_man_app.repository.UserRepository;

@Service
public class UserService {

	private  UserRepository userRepository;
	
	UserService(UserRepository userRepository){
		this.userRepository=userRepository;
	}
	public User registerUser(User user) {
		return userRepository.save(user);
	}
	public User fetchUserByUsername(String username) throws UserNotFoundException{
		if(userRepository.findByUsername(username)==null) {
			throw new UserNotFoundException("User not found with username: "+username);
		}
		else {
		 return userRepository.findByUsername(username);
		}
	}
	
}
