package dev.jartur.oitava.controller;

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

import dev.jartur.oitava.model.Patient;
import dev.jartur.oitava.repository.PatientRepository;

@RestController
@RequestMapping("/patients")
public class PatientController {
  @Autowired
  PatientRepository patientRepository;

  @GetMapping
  public ResponseEntity<List<Patient>> getPatients() {
    return ResponseEntity.ok(patientRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Patient> getPatient(@PathVariable Long id) {
    return ResponseEntity.ok(patientRepository.findById(id).get());
  }

  @PostMapping
  public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
    var existingPatient = patientRepository.findByEmail(patient.getEmail());
    if (existingPatient.isPresent()) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "E-mail já cadastrado");
      return ResponseEntity.badRequest().body(response);
    }

    existingPatient = patientRepository.findByCpf(patient.getCpf());
    if (existingPatient.isPresent()) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "CPF já cadastrado");
      return ResponseEntity.badRequest().body(response);
    }

    existingPatient = patientRepository.findByRg(patient.getRg());
    if (existingPatient.isPresent()) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "RG já cadastrado");
      return ResponseEntity.badRequest().body(response);
    }

    return ResponseEntity.ok(patientRepository.save(patient));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
    var existingPatient = patientRepository.findByEmail(patient.getEmail());
    if (existingPatient.isPresent() && !existingPatient.get().getId().equals(id)) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "E-mail já cadastrado");
      return ResponseEntity.badRequest().body(response);
    }

    existingPatient = patientRepository.findByCpf(patient.getCpf());
    if (existingPatient.isPresent() && !existingPatient.get().getId().equals(id)) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "CPF já cadastrado");
      return ResponseEntity.badRequest().body(response);
    }

    existingPatient = patientRepository.findByRg(patient.getRg());
    if (existingPatient.isPresent() && !existingPatient.get().getId().equals(id)) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "RG já cadastrado");
      return ResponseEntity.badRequest().body(response);
    }

    patient.setId(id);
    return ResponseEntity.ok(patientRepository.save(patient));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
    patientRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

}
