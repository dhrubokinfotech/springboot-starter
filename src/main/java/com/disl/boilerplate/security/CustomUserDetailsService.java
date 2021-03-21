package com.disl.boilerplate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.disl.boilerplate.models.User;
import com.disl.boilerplate.services.UserService;



@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	UserService loginService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User loggedUser = loginService.findByEmail(username);
		if (loggedUser == null) {
			throw new UsernameNotFoundException("User not found.");
		} else {
			return CustomUserDetails.create(loggedUser);
		}
	}
	
	@Transactional
    public UserDetails loadUserById(String username) {
        User user = loginService.findByEmail(username);
        		if (user == null) {
        			throw new UsernameNotFoundException("User not found with id : " + username);
        		}
        return CustomUserDetails.create(user);
    }
}
