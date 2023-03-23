package com.disl.starter.thymleaf.controller;

import com.disl.starter.constants.AppUtils;
import com.disl.starter.entities.User;
import com.disl.starter.services.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StarterErrorController implements ErrorController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/error", produces = MediaType.TEXT_HTML_VALUE)
    public String handleError(HttpServletRequest request) {
        User user = AppUtils.getLoggedInUser(userService);
        if (user != null) {
            return "redirect:/dashboard";
        }

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error-404";
            } else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                return "error-403";
            }
        }

        return "error";
    }

    @GetMapping("/permission-denied")
    public ModelAndView permissionDenied(){
        User user = AppUtils.getLoggedInUser(userService);
        if (user != null) {
            return new ModelAndView("redirect:/dashboard");
        }

        return new ModelAndView("permission-denied");
    }
}
