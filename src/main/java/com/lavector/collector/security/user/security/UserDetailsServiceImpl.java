package com.lavector.collector.security.user.security;

import com.lavector.collector.entity.user.User;
import com.lavector.collector.entity.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author tao
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        logger.info("Loading user: username=" + username);

        User user = null;
        if (username.indexOf("@") < 0) {
            user = userService.findByUsername(username);
        } else {
            user = userService.findByEmail(username);
        }

        if (user == null || !user.isActive()) {
            //todo
            throw new UsernameNotFoundException("username not found");
        }


//        logger.info("User is " + user.toString());
        return new UserDetailsImpl(user);
    }
}
