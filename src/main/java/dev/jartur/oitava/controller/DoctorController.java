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
  public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor) {
    System.out.println(doctor);

    var existingDoctor = doctorRepository.findByEmail(doctor.getEmail());
    if (existingDoctor.isPresent()) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "E-mail já cadastrado");
      return ResponseEntity.badRequest().body(response);
    }

    existingDoctor = doctorRepository.findByCpf(doctor.getCpf());
    if (existingDoctor.isPresent()) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "CPF já cadastrado");
      return ResponseEntity.badRequest().body(response);
    }


    return ResponseEntity.ok(doctorRepository.save(doctor));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
    var existingDoctor = doctorRepository.findByEmail(doctor.getEmail());
    if (existingDoctor.isPresent() && !existingDoctor.get().getId().equals(id)) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "E-mail já cadastrado");
      return ResponseEntity.badRequest().body(response);
    }

    existingDoctor = doctorRepository.findByCpf(doctor.getCpf());
    if (existingDoctor.isPresent() && !existingDoctor.get().getId().equals(id)) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "CPF já cadastrado");
      return ResponseEntity.badRequest().body(response);
    }

    doctor.setId(id);
    return ResponseEntity.ok(doctorRepository.save(doctor));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
    doctorRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

}
