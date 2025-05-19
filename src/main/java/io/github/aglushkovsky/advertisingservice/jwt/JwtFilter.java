package io.github.aglushkovsky.advertisingservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static io.github.aglushkovsky.advertisingservice.jwt.JwtUtils.createJwtAuthentication;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtils jwtUtils;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String accessTokenFromRequest = getAccessTokenFromRequest(request);

            if (accessTokenFromRequest != null) {
                jwtUtils.validateAccessToken(accessTokenFromRequest);
                Claims claimsFromToken = jwtUtils.getClaimsFromToken(accessTokenFromRequest);
                JwtAuthentication jwtAuthentication = createJwtAuthentication(claimsFromToken);
                jwtAuthentication.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
            }

            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            SecurityContextHolder.clearContext();
            JwtAuthenticationException authenticationException = new JwtAuthenticationException("Invalid token", e);
            jwtAuthenticationEntryPoint.commence(request, response, authenticationException);
        }
    }

    private String getAccessTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        return token != null && token.startsWith(BEARER_PREFIX)
                ? token.substring(7)
                : null;
    }
}
