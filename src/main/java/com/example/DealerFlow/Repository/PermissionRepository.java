package com.example.DealerFlow.Repository;

import com.example.DealerFlow.Model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Optional<Permission> findByName(String name);

    boolean existsByName(String name);
}
