package com.disl.starter.exceptions;

import com.disl.starter.StarterApplication;
import com.disl.starter.entities.Role;
import com.disl.starter.enums.RoleType;
import com.disl.starter.security.CustomUserDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            StarterApplication.logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        System.out.println(response.getHeader("Authorization"));
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        boolean isGranted = false;

        try {
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            Optional<Role> roleOptional = principal.getRoles().stream().findFirst();

            if(roleOptional.isPresent()) {
                isGranted = roleOptional.get().getRoleType() == RoleType.ADMIN;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            for (GrantedAuthority grantedAuthority : authorities) {
                System.out.println(grantedAuthority.getAuthority());

                if (grantedAuthority.getAuthority().startsWith("ROLE_")) {
                    isGranted = true;
                    break;
                }
            }
        }

        if (isGranted) {
            return "/dashboard";
        }

        SecurityContextHolder.getContext().setAuthentication(null);
        return "/permission-denied";
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }
}