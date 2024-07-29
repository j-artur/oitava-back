package dev.jartur.oitava.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jartur.oitava.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
  Optional<Patient> findByEmail(String email);
  Optional<Patient> findByCpf(String cpf);
  Optional<Patient> findByRg(String rg);

}
