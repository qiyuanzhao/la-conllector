package com.lavector.collector.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * @author tao
 */
@Component(value = "restAuthenticationFailureHandler")
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {
	
    private static final Logger logger = LoggerFactory.getLogger(RestAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logger.warn("!!!!!!!!! LOGIN FAILURE !!!!!!!!!!!");
        response.addHeader("loginFailureMessageKey", "EXCEPTION_LOGIN_FAILED");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
