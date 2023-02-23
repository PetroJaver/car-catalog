package com.implemica.security;

import com.implemica.model.entity.User;
import com.implemica.model.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementing the {@link UserDetailsService} interface.
 */
@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    /**
     * The UserRepository used to find admin users in the database.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Find user in database, and convert to {@link UserDetails}.
     *
     * @param username the username identifying the user whose data is required.
     * @return {@link UserDetails} containing user details fetched from the database.
     * @throws {@link UsernameNotFoundException} will be thrown if a user with that {@code username} is not found in the database.
     */
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));

        return SecurityUser.fromUser(user);
    }
}
