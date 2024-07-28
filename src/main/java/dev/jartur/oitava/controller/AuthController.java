package dev.jartur.oitava.controller;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import dev.jartur.oitava.config.SecurityConfig;
import dev.jartur.oitava.model.User;
import dev.jartur.oitava.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@RestController
@RequestMapping
public class AuthController {

  private final AuthenticationManager authenticationManager;

  public AuthController(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Autowired
  private UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<User> signup(@RequestBody SignUpRequest request) {

    User user = new User();
    user.setNome(request.nome);
    user.setEmail(request.email);
    user.setSenha(request.senha);

    return ResponseEntity.ok(userService.save(user));
  }

  @PostMapping("/signin")
  public ResponseEntity<Map<String, String>> signin(@RequestBody SignInRequest request) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(request.email, request.senha));
    var user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
    Algorithm algorithm = Algorithm.HMAC256(Base64.getDecoder().decode(SecurityConfig.JWT_SECRET));

    String accessToken = JWT.create()
        .withSubject(user.getUsername())
        .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
        .withClaim("roles",
            user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
        .sign(algorithm);

    Map<String, String> response = new HashMap<>();
    response.put("accessToken", accessToken);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/me")
  public ResponseEntity<User> me(HttpServletRequest request) {
    String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    User user = userService.findByEmail(email);

    return ResponseEntity.ok(user);
  }

}

@AllArgsConstructor
class SignUpRequest {
  @NonNull
  @NotNull(message = "Nome é obrigatório")
  public String nome;
  @NotNull(message = "Email é obrigatório")
  public String email;
  @NotNull(message = "Senha é obrigatória")
  public String senha;
}

@AllArgsConstructor
class SignInRequest {
  @NotNull(message = "Email é obrigatório")
  public String email;
  @NotNull(message = "Senha é obrigatória")
  public String senha;
}
