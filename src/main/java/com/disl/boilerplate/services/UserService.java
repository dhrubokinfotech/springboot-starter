package com.disl.boilerplate.services;

import com.disl.boilerplate.entities.Role;
import com.disl.boilerplate.entities.User;
import com.disl.boilerplate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User findByEmail(String email) {
		Optional<User> loggedUser = userRepository.findByEmail(email);
		if (loggedUser.isPresent() == true) {
			return loggedUser.get();
		} else {
			return null;
		}
	}

	public User findByPasswordResetToken(String token) {
		Optional<User> user = userRepository.findByPasswordResetToken(token);
		if (user.isPresent() == true) {
			return user.get();
		} else {
			return null;
		}
	}

	public User findById(long id) {
		Optional<User> loggedUser = userRepository.findById(id);
		if (loggedUser.isPresent() == true) {
			return loggedUser.get();
		} else {
			return null;
		}
	}

	public User saveUser(User user) {
		return this.userRepository.save(user);
	}
	

	public List<User> getAllUsersList() {
		return userRepository.findAll();
	}

	
	public List<User> getAllUsersByRole (Role role) {
		return userRepository.findByRoles(role);
	} 
	
	public List<User> getAllUsersByRolesIn (Role[] roles) {
		return userRepository.findByRolesIn(roles);
	} 
	
	
}
