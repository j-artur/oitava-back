package dev.jartur.oitava.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
@Table(name = "appointments")
public class Appointment {
  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, updatable = false)
  private Long id;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "medico_id")
  private Doctor medico;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "paciente_id")
  private Patient paciente;

  private String motivo;

  @NotNull
  private LocalDateTime dataHora;

  private String local;

  private String observacoes;
}
