package com.implemica.controller;

import com.implemica.model.dto.AuthenticationRequestDTO;
import com.implemica.model.dto.AuthorizationResponse;
import com.implemica.model.entity.User;
import com.implemica.model.repository.UserRepository;
import com.implemica.security.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@Api(tags = "Authentication", description = "Operation with admin")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationRestController(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping(value = "/login", produces = "application/json")
    @ApiOperation(value = "Logs admin into the system.", notes = "Use this API login endpoint to access admin functionality." +
            " To log in, you need an email and password for this, in json format," +
            " as shown in the example. If the data is correct," +
            " the server will answer you with a 200 code and in the body of the response " +
            "you will receive a jwt token that is needed to authenticate requests for adding, deleting and updating.")
        @ApiResponses(value = {
                @ApiResponse(code = 200, message = "Successful operation.. You have received a token to authenticate admin operations"),
                @ApiResponse(code = 403, message = "Operation failed. You probably entered an unregistered email or incorrect password."),
                @ApiResponse(code = 400, message = "Operation failed. You probably entered an invalid password or email.")
        })
    public ResponseEntity<AuthorizationResponse> authenticate(@Parameter(description = "In order to successfully receive a response from the api," +
            " you should send the body following the example with the login email and the correct password.")
                                                                  @RequestBody() @Valid AuthenticationRequestDTO body) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword()));
            User user = userRepository.findByUsername(body.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
            String token = jwtTokenProvider.createToken(body.getUsername(), user.getRole().name());
            AuthorizationResponse authorizationResponse = AuthorizationResponse.builder().username(body.getUsername()).token(token).build();
            return new ResponseEntity<>(authorizationResponse, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity("Invalid email/password combination", HttpStatus.FORBIDDEN);
        }
    }
}
