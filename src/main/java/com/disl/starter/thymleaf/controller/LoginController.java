package com.disl.starter.thymleaf.controller;

import com.disl.starter.constants.AppUtils;
import com.disl.starter.entities.User;
import com.disl.starter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ModelAndView login(){
        User user = AppUtils.getLoggedInUser(userService);
        if (user != null) {
            return new ModelAndView("redirect:/dashboard");
        }

        return new ModelAndView("login");
    }

    @GetMapping("/login-error")
    public ModelAndView errorLogin(Map<String,String> map){
        User user = AppUtils.getLoggedInUser(userService);
        if (user != null) {
            return new ModelAndView("redirect:/dashboard");
        }

        map.put("problem","true");
        map.put("error", "Invalid email and password");
        return new ModelAndView("login");
    }
}
