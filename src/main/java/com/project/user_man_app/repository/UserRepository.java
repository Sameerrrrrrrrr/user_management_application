package com.project.user_man_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.user_man_app.model.User;
@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	User findByUsername(String username);
	
}
