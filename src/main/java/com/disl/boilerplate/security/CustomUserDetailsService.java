package com.disl.boilerplate.security;

import com.disl.boilerplate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.disl.boilerplate.entities.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User loggedUser = userRepository.findTopByEmail(username).orElse(null);
		if (loggedUser == null) {
			throw new UsernameNotFoundException("User not found with username");
		}

		return CustomUserDetails.create(loggedUser);
	}
	
	@Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with id : " + id);
		}

        return CustomUserDetails.create(user);
    }
}
