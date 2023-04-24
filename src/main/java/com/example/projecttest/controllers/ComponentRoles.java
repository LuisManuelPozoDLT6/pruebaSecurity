package com.example.projecttest.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class ComponentRoles implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy= new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        boolean hasAdminRole= false;
        boolean hasUserRole= false;

        Collection<? extends GrantedAuthority> authorities=
                authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority: authorities){
            if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")){
                hasAdminRole= true;
                break;
            }else if(grantedAuthority.getAuthority().equals("ROLE_USER")){
                hasUserRole=true;
                break;
            }
        }
        if (hasAdminRole) {
            redirectStrategy.sendRedirect(request,response,"/helloAdmin");
        }else if(hasUserRole){
            redirectStrategy.sendRedirect(request,response,"/helloUser");
        }else {
            redirectStrategy.sendRedirect(request,response,"/login");
        }

    }
}
