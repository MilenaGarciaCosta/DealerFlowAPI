package com.example.DealerFlow.Service;

import com.example.DealerFlow.Dto.CreateUserRequest;
import com.example.DealerFlow.Model.Role;
import com.example.DealerFlow.Model.User;
import com.example.DealerFlow.Repository.RoleRepository;
import com.example.DealerFlow.Repository.UserRepository;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private DealerService dealerService;

    @InjectMocks
    private UserService userService;

    @Test
    void deveCriarUsuarioComSucesso() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Novo Usuario");
        request.setEmail("novo@teste.com");
        request.setPassword("senha123");
        request.setRoleName("ROLE_USER");

        Role role = new Role("ROLE_USER", "Desc");

        User savedUser = new User("senhaCriptografada", "Novo Usuario", "novo@teste.com");
        savedUser.setId(1);
        savedUser.setRole(role);

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("senhaCriptografada");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser(request);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("novo@teste.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExistir() {
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("existente@teste.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Email is already registered");

        verify(userRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoRoleNaoExistir() {
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("novo@teste.com");
        request.setRoleName("ROLE_INEXISTENTE");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(roleRepository.findByName("ROLE_INEXISTENTE")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Role 'ROLE_INEXISTENTE' not found");
    }
}