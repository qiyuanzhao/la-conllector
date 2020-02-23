package com.lavector.collector.security.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetails() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        return passwordEncoder;
        return new CustomerPasswordEncoder();
    }

    class CustomerPasswordEncoder implements PasswordEncoder {
        @Override
        public String encode(CharSequence charSequence) {
            return String.valueOf(charSequence);
        }

        @Override
        public boolean matches(CharSequence charSequence, String s) {
            return String.valueOf(charSequence).equals(s);
        }
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetails());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = authProvider();
        provider.setUserDetailsService(userDetails());
        try {
            provider.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ProviderManager(Collections.singletonList(provider));
    }
}
