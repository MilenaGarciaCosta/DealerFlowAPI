package com.example.DealerFlow.Controller;

import com.example.DealerFlow.Dto.CreateUserRequest;
import com.example.DealerFlow.Dto.UserDto;
import com.example.DealerFlow.Model.Role;
import com.example.DealerFlow.Model.User;
import com.example.DealerFlow.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void deveRetornarStatus201AoCriarUsuario() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Teste");
        request.setEmail("teste@teste.com");

        User mockUser = new User("senha", "Teste", "teste@teste.com");
        mockUser.setId(1);
        mockUser.setRole(new Role("ROLE_USER", ""));

        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(mockUser);

        ResponseEntity<UserDto> response = userController.createUser(request);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody().getEmail()).isEqualTo("teste@teste.com");
    }

    @Test
    void deveRetornarStatus200AoBuscarUsuarioLogado() {
        UserDetails mockPrincipal = mock(UserDetails.class);
        when(mockPrincipal.getUsername()).thenReturn("logado@teste.com");

        User mockUser = new User("senha", "Logado", "logado@teste.com");
        mockUser.setId(1);
        mockUser.setRole(new Role("ROLE_USER", ""));

        when(userService.findByEmail("logado@teste.com")).thenReturn(Optional.of(mockUser));

        ResponseEntity<UserDto> response = userController.getCurrentUser(mockPrincipal);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().getEmail()).isEqualTo("logado@teste.com");
    }
}