package dev.jartur.oitava.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jartur.oitava.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
