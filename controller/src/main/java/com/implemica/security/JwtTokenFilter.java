package com.implemica.security;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Used to override the default filtering provided by {@link org.springframework.web.filter.GenericFilterBean}.
 *
 * @see JwtTokenProvider
 */
@Component
public class JwtTokenFilter extends GenericFilterBean {
    /**
     * The JWT token provider that this filter uses to validate JWT tokens.
     */
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * The doFilter method of the Filter is called by the container each time a request/response pair is passed through
     * the chain due to a client request for a resource at the end of the chain. The FilterChain passed in to this method
     * allows the Filter to pass on the request and response to the next entity in the chain.
     *
     * @param servletRequest  the ServletRequest object contains the client's request.
     * @param servletResponse the ServletResponse object contains the filter's response.
     * @param filterChain     the FilterChain for invoking the next filter or the resource.
     * @see JwtAuthenticationException
     * @see ServletRequest
     * @see ServletResponse
     * @see FilterChain
     */
    @Override
    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JwtAuthenticationException e) {
            SecurityContextHolder.clearContext();
            ((HttpServletResponse) servletResponse).setStatus(e.getHttpStatus().value());
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
