package com.example.DealerFlow.Service;

import com.example.DealerFlow.Domain.User;
import com.example.DealerFlow.Repository.UserRepository;
import com.example.DealerFlow.dto.AuthResponse;
import com.example.DealerFlow.dto.LoginRequest;
import com.example.DealerFlow.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

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
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getId());
        AuthResponse.UserSummary summary = new AuthResponse.UserSummary(
                user.getId(),
                user.getName(),
                user.getEmail()
        );

        return new AuthResponse(token, summary);
    }
}
