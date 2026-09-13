package com.example.DealerFlow.Controller;

import com.example.DealerFlow.Dto.AuthResponse;
import com.example.DealerFlow.Dto.LoginRequest;
import com.example.DealerFlow.Service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void deveRetornarStatus200QuandoLoginForValido() {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@teste.com");
        request.setPassword("senha123");

        AuthResponse mockResponse = new AuthResponse("token-falso-123", null);
        when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().getToken()).isEqualTo("token-falso-123");
    }
}