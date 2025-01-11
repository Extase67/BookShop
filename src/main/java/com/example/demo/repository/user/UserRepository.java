package com.example.demo.repository.user;

import com.example.demo.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findUserByEmail(@NotBlank @Email String email);
}
