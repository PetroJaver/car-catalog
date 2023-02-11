package com.implemica.controller;

import com.implemica.model.dto.AuthenticationRequestDTO;
import com.implemica.model.dto.AuthorizationResponse;
import com.implemica.model.entity.User;
import com.implemica.model.repository.UserRepository;
import com.implemica.security.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController
@Api(tags = "Authentication", description = "Operation with admin")
public class AuthenticationRestController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Logs admin into the system.",
            description = "Use this API login endpoint to access admin functionality.car table To log in, you need an email and password for this, in json format,car table as shown in the example. If the data is correct,car table the server will answer you with a 200 code and in the body of the response car tableyou will receive a jwt token that is needed to authenticate requests for adding, deleting and updating.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation.. You have received a token to authenticate admin operations"),
                    @ApiResponse(responseCode = "403", description = "Operation failed. You probably entered an unregistered email or incorrect password."),
                    @ApiResponse(responseCode = "400", description = "Operation failed. You probably entered an invalid password or email.")
            }
    )
    public ResponseEntity<AuthorizationResponse> authenticate(
            @Parameter(description = "In order to successfully receive a response from the api,car table you should send the body following the example with the login email and the correct password.")
            @RequestBody @Valid AuthenticationRequestDTO body) {
        body.setUsername(body.getUsername().toLowerCase());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword()));

        User user = userRepository.findByUsername(body.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));

        String token = jwtTokenProvider.createToken(body.getUsername(), user.getRole().name());
        AuthorizationResponse authorizationResponse = new AuthorizationResponse(body.getUsername(),token);

        return new ResponseEntity<>(authorizationResponse, HttpStatus.OK);
    }

    /**
     * Handles {@link AuthenticationException} thrown in {@link AuthenticationRestController},
     * and returns the client error code 403 "message" - "Invalid email/password combination!"
     *
     * @return body of the response.
     * @see AuthenticationException
     */
    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public Map<String, String> handleDataIntegrityViolationException() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Invalid email/password combination!");

        return response;
    }
}
