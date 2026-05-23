package com.example.DealerFlow.Service;

import com.example.DealerFlow.Dto.CreateUserRequest;
import com.example.DealerFlow.Model.Role;
import com.example.DealerFlow.Model.User;
import com.example.DealerFlow.Repository.RoleRepository;
import com.example.DealerFlow.Repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

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
        boolean emailExists = userRepository.existsByEmail(request.getEmail());

        if (emailExists) {
            throw new BadCredentialsException("Email is already registered");
        }

        String roleName = request.getRoleName();
        if (roleName == null || roleName.trim().isEmpty()) {
            throw new BadCredentialsException("Role name is required");
        }

        Role role = roleRepository.findByName(roleName.toUpperCase())
                .orElseThrow(() -> new BadCredentialsException(
                        "Role '" + roleName + "' not found"));

        User user = new User(
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getEmail()
        );
        user.setRole(role);
        user.setDealer(request.getDealer());

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
