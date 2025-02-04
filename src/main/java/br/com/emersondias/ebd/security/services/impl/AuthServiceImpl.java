package br.com.emersondias.ebd.security.services.impl;

import br.com.emersondias.ebd.security.dtos.TokenResponseDTO;
import br.com.emersondias.ebd.security.jwt.JwtTokenProvider;
import br.com.emersondias.ebd.security.services.interfaces.AuthService;
import br.com.emersondias.ebd.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    @Override
    public TokenResponseDTO authenticate(String username, String password) {
        requireNonNull(username);
        requireNonNull(password);
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var tokenResponse = JwtTokenProvider.buildAccessToken(SecurityUtils.getAuthenticatedUser());
        return tokenResponse;
    }

    @Override
    public TokenResponseDTO refreshToken() {
        var userAuthenticated = SecurityUtils.getAuthenticatedUser();
        return JwtTokenProvider.buildAccessToken(userAuthenticated);
    }

}
