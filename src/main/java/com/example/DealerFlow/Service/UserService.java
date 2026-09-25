package com.example.DealerFlow.Service;

import com.example.DealerFlow.Dto.CreateUserRequest;
import com.example.DealerFlow.Model.Role;
import com.example.DealerFlow.Model.User;
import com.example.DealerFlow.Repository.RoleRepository;
import com.example.DealerFlow.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final DealerService dealerService;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            DealerService dealerService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.dealerService = dealerService;
    }

    public User createUser(CreateUserRequest request) {
        log.info("USER_CREATE_ATTEMPT: Tentativa de criação de usuário para o email: {}", request.getEmail());

        boolean emailExists = userRepository.existsByEmail(request.getEmail());

        if (emailExists) {
            log.warn("USER_CREATE_FAILURE: O email {} já está registrado no sistema.", request.getEmail());
            throw new BadCredentialsException("Email is already registered");
        }

        String roleName = request.getRoleName();
        if (roleName == null || roleName.trim().isEmpty()) {
            log.warn("USER_CREATE_FAILURE: Tentativa de cadastro sem Role definida.");
            throw new BadCredentialsException("Role name is required");
        }

        Role role = roleRepository.findByName(roleName.toUpperCase())
                .orElseThrow(() -> {
                    log.warn("USER_CREATE_FAILURE: A Role '{}' informada não existe.", roleName);
                    return new BadCredentialsException("Role '" + roleName + "' not found");
                });

        User user = new User(
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getEmail()
        );
        user.setRole(role);
        user.setDealer(request.getDealer());

        User savedUser = userRepository.save(user);
        log.info("USER_CREATE_SUCCESS: Usuário com email {} criado com sucesso no banco de dados.", request.getEmail());

        return savedUser;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}