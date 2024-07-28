package dev.jartur.oitava.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, updatable = false)
  private Long id;

  @NotNull
  private String nome;

  @Column(unique = true)
  @NotNull
  private String email;

  @JsonIgnore
  @NotNull
  private String senha;

  public List<SimpleGrantedAuthority> getRoles() {
    return List.of(new SimpleGrantedAuthority("user"));
  }
}
