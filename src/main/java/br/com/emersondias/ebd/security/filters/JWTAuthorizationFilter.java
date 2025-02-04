package br.com.emersondias.ebd.security.filters;

import br.com.emersondias.ebd.security.enums.TokenUsageType;
import br.com.emersondias.ebd.security.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

import static java.util.Objects.nonNull;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserDetailsService userDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
    }

    private static boolean isRefreshTokenRequest(HttpServletRequest request) {
        return request.getRequestURI().equals("/auth/refresh-token") && isPostRequest(request);
    }

    private static boolean isPostRequest(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.POST.name());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        UsernamePasswordAuthenticationToken auth;

        if (nonNull(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (isRefreshTokenRequest(request)) {
                auth = processRefreshToken(token);
            } else {
                auth = processAccessToken(token);
            }
            if (nonNull(auth)) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken processAccessToken(String token) {
        if (JwtTokenProvider.validate(token, TokenUsageType.ACCESS_TOKEN)) {
            return getAuthentication(JwtTokenProvider.getUsername(token));
        }
        return null;
    }

    private UsernamePasswordAuthenticationToken processRefreshToken(String token) {
        if (JwtTokenProvider.validate(token, TokenUsageType.REFRESH_TOKEN)) {
            return getAuthentication(JwtTokenProvider.getUsername(token));
        }
        return null;
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String username) {
        UserDetails user = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

}
