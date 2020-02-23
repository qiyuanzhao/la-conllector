package com.lavector.collector.security.user.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.ArrayList;

/**
 * SpringSecurity custom Authentication to hold special custom Principals.
 *
 * @author hchen
 */
public class CustomAuthentication extends AbstractAuthenticationToken {
    //~ Static fields/initializers =================================================================

    private static final long serialVersionUID = 1L;

    //~ Instance fields ============================================================================

    private Authentication delegated;
    private Principal principal;

    //~ Constructors ===============================================================================

    public CustomAuthentication(Principal principal) {
        super(new ArrayList<GrantedAuthority>());
        setAuthenticated(true);
        this.principal = principal;
    }

    //~ Methods ====================================================================================

    public Object getCredentials() {
        return null;
    }

    public void setDelegated(Authentication delegated) {
        this.delegated = delegated;
    }

    public Authentication getDelegated() {
        return delegated;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    public Principal getPrincipal() {
        return principal;
    }
}

