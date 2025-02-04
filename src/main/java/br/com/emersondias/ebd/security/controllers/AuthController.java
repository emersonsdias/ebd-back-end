package br.com.emersondias.ebd.security.controllers;

import br.com.emersondias.ebd.constants.RouteConstants;
import br.com.emersondias.ebd.security.dtos.CredentialsDTO;
import br.com.emersondias.ebd.security.dtos.TokenResponseDTO;
import br.com.emersondias.ebd.security.services.interfaces.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "the auth API")
@RestController
@RequestMapping(RouteConstants.AUTH_ROUTE)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Authenticates the user")
    @PostMapping(value = "/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody CredentialsDTO credentialsDTO) {
        throw new RuntimeException("Method not implemented, added only for swagger configuration, application must use default spring security call");
    }

    @Operation(summary = "Refresh the user token")
    @PostMapping(value = "/refresh-token")
    public ResponseEntity<TokenResponseDTO> refreshToken() {
        var token = authService.refreshToken();
        return ResponseEntity.ok(token);
    }
}
