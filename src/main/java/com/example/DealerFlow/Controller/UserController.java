package com.example.DealerFlow.Controller;

import com.example.DealerFlow.Domain.User;
import com.example.DealerFlow.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User createdUser = service.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}
