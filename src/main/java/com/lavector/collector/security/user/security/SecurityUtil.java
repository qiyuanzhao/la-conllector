package com.lavector.collector.security.user.security;

import com.lavector.collector.entity.user.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author hao
 */
public class SecurityUtil {

    public static void setCustomAuthentication(User user) {
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Authentication authentication = new CustomAuthentication(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static UserDetailsImpl getLavectorUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        if (auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return (UserDetailsImpl) auth.getPrincipal();
    }

    public static User getCurrentUser() {
        UserDetailsImpl userDetails = getLavectorUserDetails();
        return userDetails == null ? null : userDetails.getUser();
    }

    public static Long getCurrentUserId() {
        UserDetailsImpl userDetails = getLavectorUserDetails();
        return userDetails == null ? null : userDetails.getUser().getId();
    }

    public static String getCurrentUserName() {
        UserDetailsImpl userDetails = getLavectorUserDetails();
        return userDetails == null ? null : userDetails.getUser().getUsername();
    }


    public static boolean hasRole(String roleName) {
        boolean hasRole = false;
        User user = SecurityUtil.getCurrentUser();
        if (user != null && user.hasRole(roleName)) {
            hasRole = true;
        }
        return hasRole;
    }

    public static boolean isSystemAdmin() {
        boolean isSystemAdmin = hasRole(Role.SYSTEM_ADMIN);
        return isSystemAdmin;
    }

    public static boolean isSystemViewer() {
        boolean isSystemViewer = hasRole(Role.SYSTEM_VIEWER);
        return isSystemViewer;
    }


}
