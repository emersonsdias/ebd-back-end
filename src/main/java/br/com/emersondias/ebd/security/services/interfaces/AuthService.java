package br.com.emersondias.ebd.security.services.interfaces;

import br.com.emersondias.ebd.security.dtos.TokenResponseDTO;

public interface AuthService {

    TokenResponseDTO authenticate(String username, String password);

    TokenResponseDTO refreshToken();

}
