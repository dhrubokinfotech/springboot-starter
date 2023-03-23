package com.disl.starter.thymleaf.controller;

import com.disl.starter.constants.AppUtils;
import com.disl.starter.entities.User;
import com.disl.starter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;

//    @PreAuthorize("hasAuthority('READ_DASHBOARD')")
    @GetMapping(value = "/dashboard")
    public ModelAndView getDashboard() {
        User user = AppUtils.getLoggedInUser(userService);

        if (user == null) {
            return new ModelAndView("redirect:/login");
        }

        return new ModelAndView("dashboard");
    }
}


