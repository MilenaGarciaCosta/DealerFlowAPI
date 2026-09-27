package com.example.DealerFlow.Controller;

import com.example.DealerFlow.Service.AuthService;
import com.example.DealerFlow.Dto.AuthResponse;
import com.example.DealerFlow.Dto.LoginRequest;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Autenticação de usuários e emissão de tokens JWT.")
public class AuthController {

    private final AuthService authService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário", description = "Valida e-mail e senha e retorna um token JWT junto aos dados públicos do usuário. Esta operação é pública.")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticação concluída e token emitido."),
            @ApiResponse(responseCode = "400", description = "E-mail ou senha ausente ou inválido."),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas.")
        })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("AUTH_ATTEMPT: Iniciando processo de login...");

        try {
            AuthResponse response = authService.login(request);
            log.info("AUTH_SUCCESS: Login concluído com sucesso e token gerado para o email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            log.warn("AUTH_FAILED: Tentativa de login barrada. Credenciais inválidas para o email: {}", request.getEmail());
            throw e;
        }
    }
}