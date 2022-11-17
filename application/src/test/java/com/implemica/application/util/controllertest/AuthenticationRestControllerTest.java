package com.implemica.application.util.controllertest;

import com.implemica.model.entity.User;
import com.implemica.model.enums.Role;
import com.implemica.model.enums.Status;
import com.implemica.model.repository.UserRepository;
import com.implemica.security.JwtTokenProvider;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private final User userTest = new User(1L,"test@test.email"
            ,"$2a$12$qV1GJcrL.vnDhY4G5c1v..V9w6tRxDncJqhCzMTzOlChn/927.8oW"
            ,"Test","Test", Role.ADMIN, Status.ACTIVE);


    @Test
    public void authenticateTest() throws Exception{
        when(userRepository.findByEmail(eq("test@test.com"))).thenReturn(Optional.of(userTest));
        when(jwtTokenProvider.createToken(eq("test@test.com"),eq("ADMIN"))).thenReturn("token");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@test.com\",\"password\":\"test\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"email\": \"test@test.com\",\"token\": \"token\"}"));

        verify(userRepository,times(1)).findByEmail(eq("test@test.com"));
        verifyNoMoreInteractions(userRepository);

        verify(jwtTokenProvider,times(1)).createToken(eq("test@test.com"),eq("ADMIN"));
    }

    @Test
    public void handleValidationExceptionsTest() throws Exception{
        when(userRepository.findByEmail(eq("test@test.com"))).thenReturn(Optional.of(userTest));
        when(jwtTokenProvider.createToken(eq("test@test.com"),eq("ADMIN"))).thenReturn("token");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@test.com\",\"password\":\"te\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"password\": \"password is length not from 4 to 16\"}"));
    }

    @Test
    public void authenticateUserNotExistTest() throws Exception{
        when(userRepository.findByEmail(eq("notExist@test.com"))).thenThrow(new UsernameNotFoundException("User doesn't exists"));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"notExist@test.com\",\"password\":\"test\"}"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Invalid email/password combination"));

        verify(userRepository,times(1)).findByEmail(eq("notExist@test.com"));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void authenticateUserAuthenticationExceptionTest() throws Exception{
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Not Authenticated") {
                    @Override
                    public String getMessage() {
                        return super.getMessage();
                    }
                });

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@test.com\",\"password\":\"test\"}"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Invalid email/password combination"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoMoreInteractions(authenticationManager);

        verifyNoInteractions(userRepository);
    }
}
