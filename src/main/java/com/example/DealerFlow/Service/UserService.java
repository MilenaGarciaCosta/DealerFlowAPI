package com.example.DealerFlow.Service;

import com.example.DealerFlow.Model.User;
import com.example.DealerFlow.Repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DealerService dealerService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, DealerService dealerService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.dealerService = dealerService;
    }

    public User createUser(User user){
        boolean emailExists = userRepository.existsByEmail(user.getEmail());

        if(emailExists){
            throw new BadCredentialsException("Email is already registered");
        }

        String category = user.getCategory();
        if (category == null || (!category.equalsIgnoreCase("admin")
                && !category.equalsIgnoreCase("manager")
                && !category.equalsIgnoreCase("developer"))) {
            throw new BadCredentialsException("Category must be admin, manager or developer");
        }

        if (category.equalsIgnoreCase("manager")) {
            if (user.getDealer() == null) {
                throw new BadCredentialsException("Dealer is required for managers");
            }
            if (!dealerService.isDealerValid(user.getDealer())) {
                throw new BadCredentialsException("Invalid Dealer Code");
            }
        }
        else {
            user.setDealer(null);
        }

        String encryptedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
