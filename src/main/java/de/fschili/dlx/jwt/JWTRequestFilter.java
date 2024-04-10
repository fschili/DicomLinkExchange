package de.fschili.dlx.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import de.fschili.dlx.impl.TokenService;
import io.jsonwebtoken.Claims;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private final static Logger log = LoggerFactory.getLogger(JWTRequestFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_TYPE_BEARER = "Bearer";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader(AUTHORIZATION_HEADER);

        String token = null;
        String jwtToken = null;
        // JWT Token is in the form "Bearer token".
        if (requestTokenHeader != null && requestTokenHeader.startsWith(AUTHORIZATION_TYPE_BEARER + " ")) {
            jwtToken = requestTokenHeader.substring(AUTHORIZATION_TYPE_BEARER.length() + 1);

            Claims claims = JWTUtil.getValidatedClaims(jwtToken);
            if (claims == null) {
                log.error("JWT token '" + jwtToken + "' is not valid.");
                return;
            }

            token = JWTUtil.extractSubject(jwtToken);
        }
        else {
            log.warn("Authorization header does not begin with 'Bearer' String.");
            return;
        }

        // if the token could be extracted from the JWT Header, inject it to the context
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // check against existing tokens
            if (TokenService.AVAILABLE_TOKENS.contains(token)) {
                log.debug("Adding token '" + token + "' from JWT subject to context.");

                Authentication authentication = new BasicAuthentication(token);
                authentication.setAuthenticated(true);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else {
                log.error("Token '" + token + "' from JWT subject does not exists in this demo server!");
            }
        }

        chain.doFilter(request, response);
    }

}
