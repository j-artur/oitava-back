package dev.jartur.oitava.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jartur.oitava.model.User;

public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByEmail(String email);
}
