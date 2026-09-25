package com.example.DealerFlow.Controller;

import com.example.DealerFlow.Service.AuthService;
import com.example.DealerFlow.Dto.AuthResponse;
import com.example.DealerFlow.Dto.LoginRequest;
import jakarta.validation.Valid;
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
public class AuthController {

    private final AuthService authService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
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