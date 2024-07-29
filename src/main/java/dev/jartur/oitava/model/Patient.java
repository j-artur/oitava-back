package dev.jartur.oitava.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import dev.jartur.oitava.util.Sex;
import lombok.Data;

@Entity
@Data
@Table(name = "patients")
public class Patient {
  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, updatable = false)
  private Long id;

  @NotNull
  private String nome;

  @NotNull
  private Sex sex;

  @NotNull
  private Date birthdate;

  @NotNull
  @Column(unique = true)
  private String cpf;

  @NotNull
  @Column(unique = true)
  private String rg;

  private String orgaoEmissor;

  private String logradouro;
  private String bairro;
  private String numero;
  private String cidade;
  private String uf;
  private String cep;

  private String telefone;

  @NotNull
  private String email;

  private String observacoes;
}
