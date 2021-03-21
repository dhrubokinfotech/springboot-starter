package com.disl.boilerplate.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.disl.boilerplate.models.Role;
import com.disl.boilerplate.models.User;
import com.disl.boilerplate.repository.UserDao;



@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	public User findByEmail(String email) {
		Optional<User> loggedUser = userDao.findByEmail(email);
		if (loggedUser.isPresent() == true) {
			return loggedUser.get();
		} else {
			return null;
		}
	}

	public User findByPasswordResetToken(String token) {
		Optional<User> user = userDao.findByPasswordResetToken(token);
		if (user.isPresent() == true) {
			return user.get();
		} else {
			return null;
		}
	}

	public User findById(long id) {
		Optional<User> loggedUser = userDao.findById(id);
		if (loggedUser.isPresent() == true) {
			return loggedUser.get();
		} else {
			return null;
		}
	}

	public User saveUser(User user) {
		return this.userDao.save(user);
	}
	

	public List<User> getAllUsersList() {
		return userDao.findAll();
	}

	
	public List<User> getAllUsersByRole (Role role) {
		return userDao.findByRoles(role);
	} 
	
	public List<User> getAllUsersByRolesIn (Role[] roles) {
		return userDao.findByRolesIn(roles);
	} 
	
	
}
