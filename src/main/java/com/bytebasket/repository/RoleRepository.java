package com.bytebasket.repository;

import com.bytebasket.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Custom query method - Spring Data JPA will implement this automatically
    Optional<Role> findByName(String name);

    boolean existsByName(String name);
}