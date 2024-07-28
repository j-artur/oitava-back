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

import dev.jartur.oitava.model.Doctor;
import dev.jartur.oitava.repository.DoctorRepository;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
  @Autowired
  DoctorRepository doctorRepository;

  @GetMapping
  public ResponseEntity<List<Doctor>> getDoctors() {
    return ResponseEntity.ok(doctorRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Doctor> getDoctor(@PathVariable Long id) {
    return ResponseEntity.ok(doctorRepository.findById(id).get());
  }

  @PostMapping
  public ResponseEntity<Doctor> createDoctor(Doctor doctor) {
    return ResponseEntity.ok(doctorRepository.save(doctor));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, Doctor doctor) {
    doctor.setId(id);
    return ResponseEntity.ok(doctorRepository.save(doctor));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
    doctorRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

}
