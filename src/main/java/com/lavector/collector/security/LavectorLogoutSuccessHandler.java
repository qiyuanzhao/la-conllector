package com.lavector.collector.security;

import com.lavector.collector.security.jwt.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author tao
 */
@Component("lavectorLogoutSuccessHandler")
public class LavectorLogoutSuccessHandler implements LogoutSuccessHandler {
    private static final Logger log = LoggerFactory.getLogger(LavectorLogoutSuccessHandler.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("_______ LAVECTOR ON LOGOUT SUCCESS_______");
        Cookie cookie = jwtTokenUtil.generateJwtCookie("");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
