package com.example.DealerFlow.Repository;

import com.example.DealerFlow.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String Email);
}
