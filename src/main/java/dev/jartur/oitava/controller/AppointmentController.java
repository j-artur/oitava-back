package dev.jartur.oitava.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jartur.oitava.model.Appointment;
import dev.jartur.oitava.repository.AppointmentRepository;
import dev.jartur.oitava.repository.DoctorRepository;
import dev.jartur.oitava.repository.PatientRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
  @Autowired
  AppointmentRepository appointmentRepository;

  @Autowired
  DoctorRepository doctorRepository;

  @Autowired
  PatientRepository patientRepository;

  @GetMapping
  public ResponseEntity<List<Appointment>> getAppointments() {
    return ResponseEntity.ok(appointmentRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Appointment> getAppointment(@PathVariable Long id) {
    return ResponseEntity.ok(appointmentRepository.findById(id).get());
  }

  @PostMapping
  public ResponseEntity<?> createAppointment(@RequestBody AppointmentDto request) {
    LocalDateTime dataHora = LocalDateTime.of(request.data.getYear(), request.data.getMonth(),
        request.data.getDayOfMonth(), request.hora.getHour(), request.hora.getMinute(), 0, 0);

    var existingAppointment = appointmentRepository.findFirstByMedicoIdAndDataHora(request.medicoId, dataHora);
    if (existingAppointment.isPresent()) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "Já existe um agendamento para este médico nesta data e hora");
      return ResponseEntity.badRequest().body(response);
    }

    existingAppointment = appointmentRepository.findFirstByPacienteIdAndDataHora(request.pacienteId, dataHora);
    if (existingAppointment.isPresent()) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "Já existe um agendamento para este paciente nesta data e hora");
      return ResponseEntity.badRequest().body(response);
    }

    var medico = doctorRepository.findById(request.medicoId)
        .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado"));

    var paciente = patientRepository.findById(request.pacienteId)
        .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));

    var appointment = new Appointment();
    appointment.setMedico(medico);
    appointment.setPaciente(paciente);
    appointment.setMotivo(request.motivo);
    appointment.setDataHora(dataHora);
    appointment.setLocal(request.local);
    appointment.setObservacoes(request.observacoes);

    return ResponseEntity.ok(appointmentRepository.save(appointment));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateAppointment(@PathVariable Long id, @RequestBody AppointmentDto request) {
    LocalDateTime dataHora = LocalDateTime.of(request.data.getYear(), request.data.getMonth(),
        request.data.getDayOfMonth(), request.hora.getHour(), request.hora.getMinute(), 0, 0);

    var actualAppointment = appointmentRepository.findById(id).orElseThrow(
        () -> new IllegalArgumentException("Agendamento não encontrado"));

    var existingAppointment = appointmentRepository
        .findFirstByMedicoIdAndDataHora(actualAppointment.getMedico().getId(), dataHora);
    if (existingAppointment.isPresent() && !existingAppointment.get().getId().equals(id)) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "Já existe um agendamento para este médico nesta data e hora");
      return ResponseEntity.badRequest().body(response);
    }

    existingAppointment = appointmentRepository
        .findFirstByPacienteIdAndDataHora(actualAppointment.getPaciente().getId(), dataHora);
    if (existingAppointment.isPresent() && !existingAppointment.get().getId().equals(id)) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "Já existe um agendamento para este paciente nesta data e hora");
      return ResponseEntity.badRequest().body(response);
    }

    var medico = doctorRepository.findById(request.medicoId)
        .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado"));

    var paciente = patientRepository.findById(request.pacienteId)
        .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));

    var appointment = appointmentRepository.findById(id).get();
    appointment.setMedico(medico);
    appointment.setPaciente(paciente);
    appointment.setMotivo(request.motivo);
    appointment.setDataHora(dataHora);
    appointment.setLocal(request.local);
    appointment.setObservacoes(request.observacoes);

    return ResponseEntity.ok(appointmentRepository.save(appointment));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
    appointmentRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

}

@AllArgsConstructor
class AppointmentDto {
  public Long medicoId;
  public Long pacienteId;
  public String motivo;
  public LocalDate data;
  public LocalTime hora;
  public String local;
  public String observacoes;
}
