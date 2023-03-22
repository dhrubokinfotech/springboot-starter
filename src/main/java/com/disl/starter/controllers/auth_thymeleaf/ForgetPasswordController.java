package com.disl.starter.controllers.auth_thymeleaf;

import com.disl.starter.config.AppProperties;
import com.disl.starter.constants.AppUtils;
import com.disl.starter.entities.User;
import com.disl.starter.models.requests.ForgetPassRequest;
import com.disl.starter.services.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Hidden
@Controller
public class ForgetPasswordController {

	@Autowired
	private UserService userService;

	@Autowired
	private AppProperties appProperties;

	@Autowired
	private PasswordEncoder passwordEncoder;

	 @GetMapping(value = "resetpassword", produces = MediaType.TEXT_HTML_VALUE)
     public String resetPasswordHTML(Model model, @RequestParam(name = "passResetToken") String passResetToken) {
        if (passResetToken == null) {
	 		model.addAttribute("message", "User Token Not Found.");
	 		return "dispatchMessage";
        }
	 
	 	User user = userService.findByPasswordResetToken(passResetToken);
	 	
	 	if (user == null) {
	 		model.addAttribute("message", "User-token mismatch.");
	 		return "dispatchMessage";
	 	} 
	 	
	 	ForgetPassRequest forgetPassRequest = new ForgetPassRequest();
	 	forgetPassRequest.setToken(passResetToken);
	 	model.addAttribute("passChnReq", forgetPassRequest);
	 	model.addAttribute("projectName", appProperties.getName());
	 	return "password-change";
	  }
	 
	 @PostMapping(value = "pass-reset", produces = MediaType.TEXT_HTML_VALUE)
	 public String resetPass(@ModelAttribute("passChnReq") ForgetPassRequest forgetPassRequest, BindingResult result,
	            Model model) {
		User user = userService.findByPasswordResetToken(forgetPassRequest.getToken()); 	
	 	if (user == null) {
			model.addAttribute("projectName", appProperties.getName());
	 		model.addAttribute("message", "User-token mismatch.");
	 		return "dispatchMessage";
	 	} 
	 	if (!forgetPassRequest.getPass1().equals(forgetPassRequest.getPass2())) {
			model.addAttribute("projectName", appProperties.getName());
	 		model.addAttribute("message", "Passwords are not same.");
	 		return "dispatchMessage";
	 	}

		 String invalidPasswordMessage = AppUtils.getInvalidPasswordMessage(forgetPassRequest.getPass1());

	 	if (invalidPasswordMessage == null) {
	 		user.setPassword(passwordEncoder.encode(forgetPassRequest.getPass1()));
	 		user.setPasswordResetToken(null);
	 		userService.saveUser(user);

			model.addAttribute("projectName", appProperties.getName());
	 		model.addAttribute("message", "Password Changed Successfully.");
		} else {
			model.addAttribute("projectName", appProperties.getName());
	 		model.addAttribute("message", invalidPasswordMessage);
		}

		 return "dispatchMessage";
	 }
}
