package com.scor.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;

public class CustomAccessDeniedHandlerImpl extends org.springframework.security.web.access.AccessDeniedHandlerImpl {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // TODO Auto-generated method stub
        response.setStatus(401);
        response.getWriter().flush();
    }

    // @Override
    // public void onAuthenticationFailure(HttpServletRequest request,
    // HttpServletResponse response,
    // AuthenticationException exception) throws IOException, ServletException {
    // switch(exception.getLocalizedMessage()){
    // case "User is disabled":
    // response.getWriter().print("{\"errCode\":\"PENDING_ACTIVATION\",\"message\":
    // \"Your account is pending activation!\"}");
    // break;
    // case "User account is locked":
    // response.getWriter().print("{\"errCode\":\"ACCOUNT_DISABLED\",\"message\":
    // \"Your account has been disabled!\"}");
    // break;
    // default:
    // response.getWriter().print("{\"errCode\":\"BAD_CREDENTIALS\",\"message\":
    // \"Bad credentials!\"}");
    // break;
    // }
    // response.setStatus(401);
    // response.getWriter().flush();
    // }
}