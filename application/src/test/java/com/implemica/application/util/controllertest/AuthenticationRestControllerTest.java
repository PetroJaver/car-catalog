package com.implemica.application.util.controllertest;

import com.implemica.model.entity.User;
import com.implemica.model.enums.Role;
import com.implemica.model.enums.Status;
import com.implemica.model.repository.UserRepository;
import com.implemica.security.JwtTokenProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private final User userTest = new User(1L, "test"
            , "$2a$12$j/uo54GHCHR/eS3/f3jHTudt/E5FOdkfQM7fBLmqR3TXLrXoULiNi", Role.ADMIN, Status.ACTIVE);


    @Test
    public void authenticateTest() throws Exception {
        when(userRepository.findByUsername(eq("test"))).thenReturn(Optional.of(userTest));
        when(jwtTokenProvider.createToken(eq("test"), eq("ADMIN"))).thenReturn("token");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"test\",\"password\":\"test\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"username\": \"test\",\"token\": \"token\"}"));

        verify(userRepository, times(1)).findByUsername(eq("test"));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void handleValidationExceptionsTest() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"test\",\"password\":\"no\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"password\": \"password is length not from 4 to 20\"}"));

        verifyNoInteractions(userRepository);
    }

    @Test
    public void authenticateUserNotExistTest() throws Exception {
        when(userRepository.findByUsername(eq("notexistadmin"))).thenReturn(Optional.empty());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"notExistAdmin\",\"password\":\"test\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("{\"message\":\"Invalid email/password combination!\"}"));

        verify(userRepository, times(1)).findByUsername(eq("notexistadmin"));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void authenticateUserAuthenticationExceptionTest() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Not Authenticated") {
                    @Override
                    public String getMessage() {
                        return super.getMessage();
                    }
                });

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"test\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("{\"message\":\"Invalid email/password combination!\"}"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoMoreInteractions(authenticationManager);

        verifyNoInteractions(userRepository);
    }
}
