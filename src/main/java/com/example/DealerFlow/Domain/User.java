package com.example.DealerFlow.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String Name;

    @NotBlank(message = "Email is required")
    @Column(nullable = false)
    private String Email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String Password;
}
