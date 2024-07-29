package dev.jartur.oitava.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jartur.oitava.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
  Optional<Appointment> findFirstByMedicoIdAndDataHora(Long medicoId, LocalDateTime dataHora);
  Optional<Appointment> findFirstByPacienteIdAndDataHora(Long pacienteId, LocalDateTime dataHora);
}
