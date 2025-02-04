package br.com.emersondias.ebd.security.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Builder
public class TokenResponseDTO implements Serializable {

    private final String access_token;
    private final String refresh_token;
    private final String token_type;
    private final long expires_in;
    private final long created_at;

}