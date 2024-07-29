package dev.jartur.oitava.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name = "doctors")
public class Doctor {
  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, updatable = false)
  private Long id;

  @NotNull
  private String nome;

  @NotNull
  private String conselho;

  @NotNull
  private String conselhoUf;

  @NotNull
  private String conselhoNum;

  private String cbo;

  @NotNull
  @Column(unique = true)
  private String cpf;

  private String logradouro;
  private String bairro;
  private String numero;
  private String cidade;
  private String uf;
  private String cep;

  private String telefone;

  @NotNull
  private String email;

  @JsonIgnore
  @OneToMany(mappedBy = "medico")
  private List<Appointment> agendamentos = new ArrayList<>();

  public void addAppointment(Appointment appointment) {
    agendamentos.add(appointment);
    appointment.setMedico(this);
  }
}
