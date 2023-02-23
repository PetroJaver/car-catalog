package com.implemica.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * The JwtTokenProvider class provides functionality to create, validate and extract information from JWT tokens.
 * It includes methods to encode a user's information and role into a token,
 * as well as methods to authenticate the user based on the token and extract the user's username from the token.
 */
@Component
public class JwtTokenProvider {
    /**
     * An instance of UserDetailsService to load user details by username.
     */
    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    /**
     * The secret key used to sign and verify the JWT token.
     */
    @Value("${application.security.jwt.secret}")
    private String secretKey;

    /**
     * The header key used to extract the JWT token from the request headers.
     */
    @Value("${application.security.jwt.header}")
    private String authorizationHeader;

    /**
     * The lifetime of the JWT token in milliseconds.
     */
    @Value("${application.security.jwt.expiration}")
    private long validityInMilliseconds;

    /**
     * Creates a token that stores information about the user (name and role),
     * the encryption algorithm, and the lifetime of the token.
     *
     * @param username user's username which login in the system.
     * @param role     user's role which login in the system.
     * @return the generated token as a string based on username and role.
     */
    public String createToken(String username, String role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Verifies that JWT token is correct and has not expired.
     *
     * @param token the string representing the JWT token.
     * @return false, if JWT token expired.
     * @throws {@link JwtAuthenticationException} thrown if the JWT token is incorrect.
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid", HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * Authenticates the username contained in the token. And gives the rights to the user.
     *
     * @param token JWT token.
     * @return the rights available to the user based on the provided token.
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * Extracts username from JWT token.
     *
     * @param token JWT token.
     * @return username content in JWT token.
     */
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Get {@link HttpServletRequest} and from request extract JWT token by key of header {@link #authorizationHeader}.
     *
     * @param request client request.
     * @return JWT token.
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(authorizationHeader);
    }
}
