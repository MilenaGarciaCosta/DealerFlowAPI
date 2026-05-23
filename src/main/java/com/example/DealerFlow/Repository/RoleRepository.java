package com.example.DealerFlow.Repository;

import com.example.DealerFlow.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);

    boolean existsByName(String name);
}
