package io.github.aglushkovsky.advertisingservice.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static io.github.aglushkovsky.advertisingservice.jwt.JwtUtils.createJwtAuthentication;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtils jwtUtils;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String accessTokenFromRequest = getAccessTokenFromRequest((HttpServletRequest) servletRequest);
        if (accessTokenFromRequest != null && jwtUtils.isValidAccessToken(accessTokenFromRequest)) {
            JwtAuthentication jwtAuthentication = createJwtAuthentication(jwtUtils.getClaimsFromToken(accessTokenFromRequest));
            jwtAuthentication.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getAccessTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        return token != null && token.startsWith(BEARER_PREFIX)
                ? token.substring(7)
                : null;
    }
}
