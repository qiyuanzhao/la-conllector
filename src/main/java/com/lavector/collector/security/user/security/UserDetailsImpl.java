package com.lavector.collector.security.user.security;

import com.lavector.collector.entity.user.User;
import org.springframework.security.core.authority.AuthorityUtils;

import java.security.Principal;

/**
 * @author tao
 */
public class UserDetailsImpl extends org.springframework.security.core.userdetails.User
        implements Principal {
    private static final long serialVersionUID = 1L;

    private User user;

    public UserDetailsImpl(User user) {
        super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserId() {
        Long userId = null;
        if (user != null) {
            userId = user.getId();
        }
        return userId;
    }

    @Override
    public String getName() {
        String name = null;
        if (user != null) {
            name = user.getUsername();
        }
        return name;
    }

}
