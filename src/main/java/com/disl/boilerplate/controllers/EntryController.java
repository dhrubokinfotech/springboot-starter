package com.disl.boilerplate.controllers;

import com.disl.boilerplate.config.AppProperties;
import com.disl.boilerplate.constants.AppConstants;
import com.disl.boilerplate.constants.AppUtils;
import com.disl.boilerplate.entities.Role;
import com.disl.boilerplate.entities.User;
import com.disl.boilerplate.models.requests.ChangePasswordRequest;
import com.disl.boilerplate.models.requests.InitialForgetPasswordRequest;
import com.disl.boilerplate.models.requests.SignInRequest;
import com.disl.boilerplate.models.requests.SignUpRequest;
import com.disl.boilerplate.models.responses.TokenResponse;
import com.disl.boilerplate.models.Response;
import com.disl.boilerplate.repository.RoleRepository;
import com.disl.boilerplate.security.CustomUserDetailsService;
import com.disl.boilerplate.security.JwtTokenProvider;
import com.disl.boilerplate.services.MailService;
import com.disl.boilerplate.services.UserService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.Map.Entry;

@RestController
public class EntryController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	RoleRepository roleRepository;
	
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    JwtTokenProvider tokenProvider;
    
    @Autowired
    UserService loginService;
    
    @Autowired
    MailService mailService;
    
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    
    @Autowired
    AppProperties appProperties;

	@ApiOperation(value = "Sign-in")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = TokenResponse.class),
	@ApiResponse(code = 401, message = "Unauthorized"),
	@ApiResponse(code = 403, message="Forbidden"),
	@ApiResponse(code = 404, message = "Not Found"),
	@ApiResponse(code = 500, message = "Failure")})
    @PostMapping(value = "/signin")
    public Response authenticateUser(@RequestBody SignInRequest loginRequest) {    
		User user = loginService.findByEmail(loginRequest.getEmail());
		if (user == null) {
			return new Response(HttpStatus.FORBIDDEN, false, "No user found with this email", null);
		}

        Authentication authentication = authenticationManager.authenticate(
           new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        if (jwt == null) {
        	return new Response(HttpStatus.FORBIDDEN, false, "Unknown error. Please try again", null);
		}
        
        return new Response(HttpStatus.OK, true, "You are now logged in.", new TokenResponse(jwt, loginService.findByEmail(loginRequest.getEmail())));
    }
	
	@ApiOperation(value = "Consumer Sign Up")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = Response.class),@ApiResponse(code = 401, message = "Unauthorized"),@ApiResponse(code = 403, message="Forbidden"),@ApiResponse(code = 404, message = "Not Found"),@ApiResponse(code = 500, message = "Failure")})
    @PostMapping(value = "/signup")
    public Response createUser (@RequestBody SignUpRequest signUpRequest) {
		User ifUserExists = loginService.findByEmail(signUpRequest.getEmail());
		if (ifUserExists != null) {
			return new Response(HttpStatus.BAD_REQUEST, false, "User with this email exists already. Please signin or try with different email", null);
		}

		String invalidPasswordMessage = AppUtils.getInvalidPasswordMessage(signUpRequest.getPassword());
		if (invalidPasswordMessage != null) {
			return new Response(HttpStatus.BAD_REQUEST, false, invalidPasswordMessage, null);
		}
		
    	User signedUser = new User();
    	signedUser.setEmail(signUpRequest.getEmail());
    	signedUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
    	signedUser.setName(signUpRequest.getName());
    	Optional<Role> role = roleRepository.findByRoleName(AppConstants.consumerRole);
    	
    	if (role.isPresent()) {
    		Set<Role> roles = new HashSet<>();
    		roles.add(role.get());
    		signedUser.setRoles(roles);
        	User savedUser = loginService.saveUser(signedUser);
        	if (savedUser != null) {        		
        		return new Response(HttpStatus.OK, true, "User Created. Please check your email address and verify your account", null);
        	} else {
    			return new Response(HttpStatus.INTERNAL_SERVER_ERROR, false, "Failed due to unknown reason.", null);
        	}
    	} else {
			return new Response(HttpStatus.INTERNAL_SERVER_ERROR, false, "User role not found. Server Error.", null);
    	}
    }

    @ApiOperation(value = "Change password request", authorizations = {@Authorization(value = "jwtToken")})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = Response.class),@ApiResponse(code = 401, message = "Unauthorized"),@ApiResponse(code = 403, message="Forbidden"),@ApiResponse(code = 404, message = "Not Found"),@ApiResponse(code = 500, message = "Failure")})
	@PreAuthorize("#changePasswordRequest.email == authentication.name")
	@PostMapping(value = "changepassword")
	public Response changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
		User login = loginService.findByEmail(changePasswordRequest.getEmail());
		if (login != null) {
			if (login.getPassword() == null) {
				new Response(HttpStatus.BAD_REQUEST, false, "Password Not available for google/facebook user.", null);
			}
			if(passwordEncoder.matches(changePasswordRequest.getPreviousPassword(),login.getPassword())) {
				String invalidPasswordMessage = AppUtils.getInvalidPasswordMessage(changePasswordRequest.getNewPassword());

				if (invalidPasswordMessage == null) {
					login.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
					loginService.saveUser(login);
					return new Response(HttpStatus.OK, true, "Password Changed successfully", null);
				} else {
					return new Response(HttpStatus.BAD_REQUEST, false, invalidPasswordMessage, null);
				}
			} else {
				return new Response(HttpStatus.BAD_REQUEST, false, "Incorrect old password.", null);
			}
		} else {
			return new Response(HttpStatus.BAD_REQUEST, false, "User not found for the id.", null);
		}
	}
	
    @ApiOperation(value = "Forget password Request")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = Response.class),@ApiResponse(code = 401, message = "Unauthorized"),@ApiResponse(code = 403, message="Forbidden"),@ApiResponse(code = 404, message = "Not Found"),@ApiResponse(code = 500, message = "Failure")})
	@PostMapping(value = "forgetpassword")
	public Response requestForgetPassword (@RequestBody InitialForgetPasswordRequest initialForgetPasswordRequest) {
		User user = loginService.findByEmail(initialForgetPasswordRequest.getUserEmail());
		if (user == null) {
			return new Response(HttpStatus.NOT_FOUND, false, "USER NOT FOUND WITH THIS EMAIL", "");
		}
		if (AppUtils.isValidMail(user.getEmail())) {
			String token = UUID.randomUUID().toString();
			user.setPasswordResetToken(token);
			loginService.saveUser(user);
			
			if(appProperties.getActiveProfile() != AppConstants.environment.development) {
				mailService.sendMail(user.getEmail(), AppConstants.forgetPasswordSubject, AppConstants.forgetPasswordText + appProperties.getBackEndUrl()+AppConstants.RESET_PASSWORD_SUBURL + token);
    		}
			
			return new Response(HttpStatus.OK, true, "Reset password link sent to your registered email address.", null);
		} else {
			return new Response(HttpStatus.EXPECTATION_FAILED, false, "Your email format is not correct.", null);
		}
	}
    
	@GetMapping(value = "/refreshtoken")
	public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
		// From the HttpRequest get the claims
		Claims claims = (Claims) request.getAttribute("claims");
		Map<String, Object> expectedMap = this.getMapFromIoJsonwebtokenClaims(claims);
		String token = tokenProvider.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
        return ResponseEntity.ok(new TokenResponse(token,null));
	}

	public Map<String, Object> getMapFromIoJsonwebtokenClaims(Claims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}

		return expectedMap;
	}
}
