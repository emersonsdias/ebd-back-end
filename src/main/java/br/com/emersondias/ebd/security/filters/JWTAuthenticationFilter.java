package br.com.emersondias.ebd.security.filters;

import br.com.emersondias.ebd.config.AppConfig;
import br.com.emersondias.ebd.dtos.errors.StandardErrorDTO;
import br.com.emersondias.ebd.security.dtos.CredentialsDTO;
import br.com.emersondias.ebd.security.jwt.JwtTokenProvider;
import br.com.emersondias.ebd.security.models.UserAuthenticated;
import br.com.emersondias.ebd.utils.LogHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;

@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final LogHelper LOG = LogHelper.getInstance();

    private final AuthenticationManager authenticationManager;
    private final Gson gson = AppConfig.gson();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            var credentials = new ObjectMapper().readValue(request.getInputStream(), CredentialsDTO.class);
            var authToken = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword(), Collections.emptyList());
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserAuthenticated userAuthenticated = (UserAuthenticated) authResult.getPrincipal();
        try {
            response.setContentType("application/json");
            var tokenResponse = JwtTokenProvider.buildAccessToken(userAuthenticated);
            response.getWriter().append(gson.toJson(tokenResponse));
        } catch (IOException e) {
            LOG.error("Failed to add token to the response", e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        response.setStatus(httpStatus.value());
        response.setContentType("application/json");
        StandardErrorDTO error = StandardErrorDTO.builder()
                .timestamp(Instant.now())
                .status(httpStatus.value())
                .error("Não autorizado")
                .message(failed.getLocalizedMessage())
                .path(request.getRequestURI())
                .build();
        response.getWriter().append(gson.toJson(error));
    }
}
