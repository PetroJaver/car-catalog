package com.implemica.config;

import com.implemica.security.JwtConfigurer;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for Spring Security. Enables web security and global method security with pre-post annotations.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    /**
     * JwtConfigurer instance that will be used to configure Spring Security for JWT authentication and authorization.
     */
    @Autowired
    private JwtConfigurer jwtConfigurer;

    /**
     * The strength factor used for the BCryptPasswordEncoder.
     */
    @Value("#{ new Integer('${application.security.bcrypt.strength.factor}')}")
    private Integer strengthFactor;

    /**
     * Configures the SecurityFilterChain instance for Spring Security.
     *
     * @param http HttpSecurity instance used to configure the filter chain
     * @return the configured SecurityFilterChain instance
     */
    @Bean
    @SneakyThrows
    protected SecurityFilterChain filterChain(HttpSecurity http) {
        http    .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .apply(jwtConfigurer);

        return http.build();
    }

    /**
     * Creates and returns the AuthenticationManager instance for Spring Security.
     *
     * @param authenticationConfiguration AuthenticationConfiguration instance used to create the AuthenticationManager
     * @return the created AuthenticationManager instance
     */
    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Returns a new instance of the BCryptPasswordEncoder class with a strength factor of {@link #strengthFactor} as a PasswordEncoder bean.
     *
     * @return a new instance of the BCryptPasswordEncoder class with a strength factor of {@link #strengthFactor}.
     */
    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(strengthFactor);
    }
}