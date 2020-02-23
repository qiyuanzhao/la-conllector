package com.lavector.collector.security.user.security;

import com.lavector.collector.entity.user.User;
import com.lavector.collector.entity.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

/**
 * @author tao
 */
@Service
public class SecurityService {

    @Autowired
    private UserService userService;
    @Autowired(required = false)
    private AuthenticationManager authenticationManager;

    public Long getCurrentUserId() {
        Long userId = SecurityUtil.getCurrentUserId();
        return userId;
    }

    public User getCurrentUser() {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null || currentUserId <= 0) {
            throw new RuntimeException("not loggin");
        }
        User u = userService.find(currentUserId);
        if (u == null) {
            throw new EntityNotFoundException();
        }
        return u;
    }

    public boolean hasRole(String roleName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority(roleName));
    }

    public void login(String username, String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        authentication = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
