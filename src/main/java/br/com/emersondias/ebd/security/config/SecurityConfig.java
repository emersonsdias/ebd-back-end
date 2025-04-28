package br.com.emersondias.ebd.security.config;

import br.com.emersondias.ebd.constants.RouteConstants;
import br.com.emersondias.ebd.entities.enums.UserRole;
import br.com.emersondias.ebd.security.filters.JWTAuthenticationFilter;
import br.com.emersondias.ebd.security.filters.JWTAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final static String[] PUBLIC_MATCHERS = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
    private final static String[] AUTH_MATCHERS = {
            RouteConstants.AUTH_ROUTE + "/**"
    };
    private final static String[] AUTH_MATCHERS_GET = {
            RouteConstants.CLASSROOMS_ROUTE + "/{classroomId}",
            RouteConstants.ITEMS_ROUTE + "/**",
            RouteConstants.LESSONS_ROUTE + "/**",
            RouteConstants.PEOPLE_ROUTE + "/birthdate",
            RouteConstants.USERS_ROUTE + "/{userId}"
    };
    private final static String[] AUTH_MATCHERS_PUT = {
            RouteConstants.LESSONS_ROUTE + "/{lessonId}",
            RouteConstants.USERS_ROUTE + "/{userId}"
    };
    private final Environment environment;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        final var activeProfiles = Arrays.asList(environment.getActiveProfiles());
        if (activeProfiles.contains("test")) {
            httpSecurity
                    .headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                    .authorizeHttpRequests(authorize -> authorize.requestMatchers(PathRequest.toH2Console()).permitAll())
                    .cors(cors -> cors
                            .configurationSource(request -> {
                                CorsConfiguration config = new CorsConfiguration();
                                config.setAllowedOrigins(List.of("*"));
                                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                                config.setAllowedHeaders(List.of("*"));
                                return config;
                            }));
        }
        if (activeProfiles.contains("dev")) {
            httpSecurity
                    .cors(cors -> cors
                            .configurationSource(request -> {
                                CorsConfiguration config = new CorsConfiguration();
                                config.setAllowedOrigins(List.of("*"));
                                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                                config.setAllowedHeaders(List.of("*"));
                                return config;
                            }));

        }
        var authenticationManager = authenticationManager(httpSecurity);
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PUBLIC_MATCHERS).permitAll()
                        .requestMatchers(AUTH_MATCHERS).authenticated()
                        .requestMatchers(HttpMethod.GET, AUTH_MATCHERS_GET).authenticated()
                        .requestMatchers(HttpMethod.PUT, AUTH_MATCHERS_PUT).authenticated()
                        .anyRequest().hasAnyRole(UserRole.ADMIN.getDescription())
                )
                .addFilter(getJWTAuthenticationFilter(authenticationManager))
                .addFilter(new JWTAuthenticationFilter(authenticationManager))
                .addFilter(new JWTAuthorizationFilter(authenticationManager, userDetailsService))
                .authenticationManager(authenticationManager(httpSecurity));
        return httpSecurity.build();
    }

    private JWTAuthenticationFilter getJWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        final JWTAuthenticationFilter filter = new JWTAuthenticationFilter(authenticationManager);
        filter.setFilterProcessesUrl(RouteConstants.AUTH_ROUTE + "/login");
        return filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}
