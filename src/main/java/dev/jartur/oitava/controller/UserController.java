package dev.jartur.oitava.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jartur.oitava.model.User;
import dev.jartur.oitava.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  UserRepository userRepository;

  @GetMapping
  public ResponseEntity<List<User>> getUsers() {
    return ResponseEntity.ok(userRepository.findAll());
  }

}
