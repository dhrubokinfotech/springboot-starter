package com.disl.boilerplate.controllers.thymeleaf;

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

import com.disl.boilerplate.constants.AppUtils;
import com.disl.boilerplate.models.User;
import com.disl.boilerplate.models.requests.ForgetPassRequest;
import com.disl.boilerplate.services.UserService;

import edu.vt.middleware.password.RuleResult;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class ForgetPasswordController {
	private final PasswordEncoder passwordEncoder;
	public ForgetPasswordController(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	@Autowired
	UserService userService;
	
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
	 	return "password-change";
   
	  }
	 
	 @PostMapping(value = "pass-reset", produces = MediaType.TEXT_HTML_VALUE)
	 public String resetPass(@ModelAttribute("passChnReq") ForgetPassRequest forgetPassRequest, BindingResult result,
	            Model model) {
		User user = userService.findByPasswordResetToken(forgetPassRequest.getToken()); 	
	 	if (user == null) {
	 		model.addAttribute("message", "User-token mismatch.");
	 		return "dispatchMessage";
	 	} 
	 	if (!forgetPassRequest.getPass1().equals(forgetPassRequest.getPass2())) {
	 		model.addAttribute("message", "Passwords are not same.");
	 		return "dispatchMessage";
	 	}
	 	RuleResult ruleResult = AppUtils.checkIfPasswordValid(forgetPassRequest.getPass1());
	 	if (ruleResult.isValid()) {
	 		user.setPassword(passwordEncoder.encode(forgetPassRequest.getPass1()));
	 		user.setPasswordResetToken(null);
	 		userService.saveUser(user);
	 		model.addAttribute("message", "Password Changed Successfully.");
	 		return "dispatchMessage"; 
	 	} else {
	 		String totalMsg = "";
	 		  for (String msg : AppUtils.getValidator(AppUtils.getPasswordRules()).getMessages(ruleResult)) {
	 			  totalMsg = totalMsg + msg + "\n";
	 		  }

	 		model.addAttribute("message", totalMsg);
	 		return "dispatchMessage";
	 	}
		 	
	 }
	 
}
