package br.com.emersondias.ebd.security.jwt;

import br.com.emersondias.ebd.security.dtos.TokenResponseDTO;
import br.com.emersondias.ebd.security.enums.TokenUsageType;
import br.com.emersondias.ebd.security.models.UserAuthenticated;
import br.com.emersondias.ebd.security.utils.SecurityUtils;
import br.com.emersondias.ebd.utils.LogHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

import static java.util.Objects.*;

@Component
public class JwtTokenProvider {

    private static final LogHelper LOG = LogHelper.getInstance();

    private static long ACCESS_TOKEN_EXPIRATION;
    private static long REFRESH_TOKEN_EXPIRATION;

    public static TokenResponseDTO buildAccessToken(UserAuthenticated userAuthenticated) {
        var iat = Instant.now();
        var accessToken = buildAccessToken(userAuthenticated, iat);
        var refreshToken = buildRefreshToken(userAuthenticated, iat);
        return TokenResponseDTO.builder()
                .access_token(accessToken)
                .refresh_token(refreshToken)
                .token_type("Bearer")
                .expires_in(ACCESS_TOKEN_EXPIRATION)
                .created_at(iat.toEpochMilli())
                .build();
    }

    private static String buildAccessToken(UserAuthenticated userAuthenticated, Instant iat) {
        return Jwts.builder()
                .subject(userAuthenticated.getUsername())
                .expiration(Date.from(iat.plusMillis(ACCESS_TOKEN_EXPIRATION)))
                .claim("iat", Date.from(iat))
                .claim("token_usage", TokenUsageType.ACCESS_TOKEN)
                .claim("user_id", userAuthenticated.getId())
                .claim("name", userAuthenticated.getName())
                .claim("roles", userAuthenticated.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .signWith(SecurityUtils.getSecretKey())
                .compact();
    }

    private static String buildRefreshToken(UserAuthenticated userAuthenticated, Instant iat) {
        return Jwts.builder()
                .subject(userAuthenticated.getUsername())
                .expiration(Date.from(iat.plusMillis(REFRESH_TOKEN_EXPIRATION)))
                .claim("iat", Date.from(iat))
                .claim("token_usage", TokenUsageType.REFRESH_TOKEN)
                .signWith(SecurityUtils.getSecretKey())
                .compact();
    }

    public static boolean validate(String token, TokenUsageType tokenUsageType) {
        try {
            if (isNull(token) || isNull(tokenUsageType)) {
                return false;
            }
            Claims claims = getClaims(token);
            if (isNull(claims)) {
                return false;
            }
            if (!tokenUsageType.equals(TokenUsageType.toEnum(claims.get("token_usage")))) {
                return false;
            }
            String username = claims.getSubject();
            if (isNull(username)) {
                return false;
            }
            Date expirationDate = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());
            return !now.after(expirationDate);
        } catch (Exception e) {
            LOG.error("Verification token failed", e);
            return false;
        }
    }

    public static String getUsername(String token) {
        return nonNull(getClaims(token)) ? requireNonNull(getClaims(token)).getSubject() : null;
    }

    private static String getKey(String token, String key) {
        Object value = requireNonNull(getClaims(token)).get(key);
        return nonNull(getClaims(token)) ? (String) value : null;
    }

    private static TokenUsageType getTokenType(String token) {
        Object tokenUsage = requireNonNull(getClaims(token)).get("token_usage");
        return nonNull(getClaims(token)) ? TokenUsageType.toEnum(requireNonNull(tokenUsage)) : null;
    }

    private static Claims getClaims(String token) {
        try {
            return Jwts.parser().verifyWith(SecurityUtils.getSecretKey()).build().parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            return null;
        }
    }

    @Value("${jwt.expiration.accessToken}")
    public void setAccessTokenExpiration(long expiration) {
        JwtTokenProvider.ACCESS_TOKEN_EXPIRATION = expiration;
    }

    @Value("${jwt.expiration.refreshToken}")
    public void setRefreshTokenExpiration(long expiration) {
        JwtTokenProvider.REFRESH_TOKEN_EXPIRATION = expiration;
    }

}
