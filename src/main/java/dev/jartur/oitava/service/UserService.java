package dev.jartur.oitava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.jartur.oitava.model.User;
import dev.jartur.oitava.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder encoder;

  public User findByEmail(String email) {
    return userRepository.findByEmail(email).orElse(null);
  }

  public User save(User user) {
    user.setSenha(encoder.encode(user.getSenha()));
    return userRepository.save(user);
  }

  public User signIn(String email, String senha) {
    var user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with that email " + email));

    if (!encoder.matches(senha, user.getSenha())) {
      throw new UsernameNotFoundException("User not found with that email " + email);
    }

    return user;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with that email " + username));

    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getSenha(), user.getRoles());
  }

}
