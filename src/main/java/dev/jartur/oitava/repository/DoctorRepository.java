package dev.jartur.oitava.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jartur.oitava.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
  Optional<Doctor> findByEmail(String email);
  Optional<Doctor> findByCpf(String cpf);
}
