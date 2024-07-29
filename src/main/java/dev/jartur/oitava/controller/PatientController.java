package dev.jartur.oitava.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  public ResponseEntity<Patient> createPatient(Patient patient) {
    return ResponseEntity.ok(patientRepository.save(patient));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Patient> updatePatient(@PathVariable Long id, Patient patient) {
    patient.setId(id);
    return ResponseEntity.ok(patientRepository.save(patient));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
    patientRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

}
