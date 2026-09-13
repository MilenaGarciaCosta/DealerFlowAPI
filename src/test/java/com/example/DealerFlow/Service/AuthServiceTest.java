package com.example.DealerFlow.Service;

import com.example.DealerFlow.Dto.AuthResponse;
import com.example.DealerFlow.Dto.LoginRequest;
import com.example.DealerFlow.Model.Role;
import com.example.DealerFlow.Model.User;
import com.example.DealerFlow.Repository.UserRepository;
import com.example.DealerFlow.Security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void deveRetornarTokenQuandoLoginForBemSucedido() {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@teste.com");
        request.setPassword("senha123");

        User user = new User();
        user.setId(1);
        user.setEmail("user@teste.com");
        user.setPassword("senhaCriptografada");
        user.setRole(new Role("ROLE_USER", "Descrição da role"));

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(any(), any(), any(), any())).thenReturn("token-falso-123");

        AuthResponse response = authService.login(request);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("token-falso-123");
        verify(userRepository, times(1)).findByEmail(request.getEmail());
    }

    @Test
    void deveLancarExcecaoQuandoEmailNaoExistir() {
        LoginRequest request = new LoginRequest();
        request.setEmail("inexistente@teste.com");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("No user registered for this email address");

        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void deveLancarExcecaoQuandoSenhaEstiverIncorreta() {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@teste.com");
        request.setPassword("senhaErrada");

        User user = new User();
        user.setPassword("senhaCriptografada");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Wrong password");
    }
}