package com.disl.starter.controllers.thymeleaf;

import com.disl.starter.config.AppProperties;
import com.disl.starter.entities.Secret;
import com.disl.starter.entities.User;
import com.disl.starter.services.SecretService;
import com.disl.starter.services.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.disl.starter.enums.UserTokenPurpose.EMAIL_VERIFICATION;

@Hidden
@Controller
public class VerificationController {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private SecretService secretService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "verify", produces = MediaType.TEXT_HTML_VALUE)
    public String userVerifyHtml(Model model, @RequestParam(name = "verificationToken") String verificationToken) {
        Secret secret = secretService.findByUserTokenAndUserTokenPurpose(verificationToken, EMAIL_VERIFICATION);
        if (secret == null) {
            model.addAttribute("projectName", appProperties.getName());
            model.addAttribute("message", "User Token Not Found.");
            return "dispatchMessage";
        }

        User user = userService.findById(secret.getUserId());
        if (user == null) {
            model.addAttribute("message", "User verification failed. Verification token is not from server. Please follow link from mail.");
            return "token not found";
        } else {
            user.setVerified(true);
            userService.saveUser(user);
            secretService.deleteSecret(secret);

            model.addAttribute("projectName", appProperties.getName());
            model.addAttribute("message", "User verified. You may now login.");
            return "dispatchMessage";
        }
    }
}
