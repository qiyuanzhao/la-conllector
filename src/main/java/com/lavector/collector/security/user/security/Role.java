package com.lavector.collector.security.user.security;

/**
 * Spring security Role needs to start with "ROLE_"
 *
 * @author tao
 */
public interface Role {
    String SYSTEM_ADMIN = "ROLE_SYSTEM_ADMIN";
    String SYSTEM_VIEWER = "ROLE_SYSTEM_VIEWER";

    String ORGANIZATION_ADMIN = "ROLE_ORGANIZATION_ADMIN";

    // scoped roles
    String USER = "ROLE_USER";
    String ADMIN = "ROLE_ADMIN";
}
