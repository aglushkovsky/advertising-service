package io.github.aglushkovsky.advertisingservice.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        Throwable cause = authException.getCause();
        if (cause instanceof ExpiredJwtException expEx) {
            handleExpiredJwtException(request, response, expEx);
        } else if (cause instanceof MalformedJwtException mjEx) {
            handleMalformedJwtException(request, response, mjEx);
        } else {
            returnDefaultResponse(request, response);
        }
    }

    private void handleMalformedJwtException(HttpServletRequest request, HttpServletResponse response, MalformedJwtException mjEx) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpServletResponse.SC_UNAUTHORIZED);
        problemDetail.setTitle("Unauthorized");
        problemDetail.setDetail("Invalid token format");

        response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
    }

    private void handleExpiredJwtException(HttpServletRequest request, HttpServletResponse response, ExpiredJwtException expEx) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpServletResponse.SC_UNAUTHORIZED);
        problemDetail.setTitle("Unauthorized");
        problemDetail.setDetail(expEx.getMessage());

        response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
    }

    private void returnDefaultResponse(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpServletResponse.SC_UNAUTHORIZED);
        problemDetail.setTitle("Unauthorized");
        problemDetail.setDetail("Something went wrong");
    }
}
