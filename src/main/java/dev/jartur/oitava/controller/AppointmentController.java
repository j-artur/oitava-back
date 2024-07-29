package dev.jartur.oitava.controller;

import java.time.LocalDateTime;
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
import dev.jartur.oitava.model.Doctor;
import dev.jartur.oitava.model.Patient;
import dev.jartur.oitava.repository.AppointmentRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
  @Autowired
  AppointmentRepository appointmentRepository;

  @GetMapping
  public ResponseEntity<List<Appointment>> getAppointments() {
    return ResponseEntity.ok(appointmentRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Appointment> getAppointment(@PathVariable Long id) {
    return ResponseEntity.ok(appointmentRepository.findById(id).get());
  }

  @PostMapping
  public ResponseEntity<?> createAppointment(@RequestBody CreateAppointment request) {
      var existingAppointment = appointmentRepository.findFirstByMedicoIdAndDataHora(request.medicoId, request.dataHora);
      if (existingAppointment.isPresent()) {
          Map<String, String> response = new HashMap<>();
          response.put("message", "Já existe um agendamento para este médico nesta data e hora");
          return ResponseEntity.badRequest().body(response);
      }

      existingAppointment = appointmentRepository.findFirstByPacienteIdAndDataHora(request.pacienteId, request.dataHora);
      if (existingAppointment.isPresent()) {
          Map<String, String> response = new HashMap<>();
          response.put("message", "Já existe um agendamento para este paciente nesta data e hora");
          return ResponseEntity.badRequest().body(response);
      }

      var appointment = new Appointment();
      var medico = new Doctor();
      medico.setId(request.medicoId);
      appointment.setMedico(medico);
      var paciente = new Patient();
      paciente.setId(request.pacienteId);
      appointment.setPaciente(paciente);
      appointment.setMotivo(request.motivo);
      appointment.setDataHora(request.dataHora);
      appointment.setLocal(request.local);
      appointment.setObservacoes(request.observacoes);

    return ResponseEntity.ok(appointmentRepository.save(appointment));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateAppointment(@PathVariable Long id, @RequestBody UpdateAppointment request) {
    var actualAppointment = appointmentRepository.findById(id).orElseThrow(
      () -> new IllegalArgumentException("Agendamento não encontrado")
    );

    var existingAppointment = appointmentRepository.findFirstByMedicoIdAndDataHora(actualAppointment.getMedico().getId(), request.dataHora);
    if (existingAppointment.isPresent() && !existingAppointment.get().getId().equals(id)) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "Já existe um agendamento para este médico nesta data e hora");
      return ResponseEntity.badRequest().body(response);
    }

    existingAppointment = appointmentRepository.findFirstByPacienteIdAndDataHora(actualAppointment.getPaciente().getId(), request.dataHora);
    if (existingAppointment.isPresent() && !existingAppointment.get().getId().equals(id)) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "Já existe um agendamento para este paciente nesta data e hora");
      return ResponseEntity.badRequest().body(response);
    }

    var appointment = appointmentRepository.findById(id).get();
    appointment.setMotivo(request.motivo);
    appointment.setDataHora(request.dataHora);
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
class CreateAppointment {
    public Long medicoId;
    public Long pacienteId;
    public String motivo;
    public LocalDateTime dataHora;
    public String local;
    public String observacoes;
}

@AllArgsConstructor
class UpdateAppointment {
    public String motivo;
    public LocalDateTime dataHora;
    public String local;
    public String observacoes;
}
