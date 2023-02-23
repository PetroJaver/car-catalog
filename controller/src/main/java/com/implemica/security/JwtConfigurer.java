package com.implemica.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * JwtConfigurer is a custom authentication configuration class that extends SecurityConfigurerAdapter
 * to configure the HttpSecurity with a JwtTokenFilter.
 *
 * @see SecurityConfigurerAdapter
 * @see DefaultSecurityFilterChain
 * @see HttpSecurity
 */
@Component
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    /**
     * The JwtTokenFilter used to authenticate requests with JWT tokens.
     */
    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    /**
     * Configures the HttpSecurity to add the JwtTokenFilter before the UsernamePasswordAuthenticationFilter.
     *
     * @param httpSecurity the HttpSecurity to configure.
     */
    @Override
    public void configure(HttpSecurity httpSecurity) {
        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
