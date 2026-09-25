package com.example.DealerFlow.Service;

import com.example.DealerFlow.Controller.AuthController;
import com.example.DealerFlow.Model.Permission;
import com.example.DealerFlow.Model.Role;
import com.example.DealerFlow.Model.User;
import com.example.DealerFlow.Repository.UserRepository;
import com.example.DealerFlow.Dto.AuthResponse;
import com.example.DealerFlow.Dto.LoginRequest;
import com.example.DealerFlow.Dto.UserDto;
import com.example.DealerFlow.Security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("No user registered for this email address"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        Role role = user.getRole();
        String roleName = role != null ? role.getName() : null;
        List<String> permissionNames = role != null && role.getPermissions() != null
                ? role.getPermissions().stream()
                    .map(Permission::getName)
                    .sorted()
                    .collect(Collectors.toList())
                : Collections.emptyList();

        String token = jwtService.generateToken(user.getEmail(), user.getId(), roleName, permissionNames);
        log.info("Token gerado com sucesso!");
        return new AuthResponse(token, UserDto.from(user));
    }
}
