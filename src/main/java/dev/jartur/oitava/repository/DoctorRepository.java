package dev.jartur.oitava.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jartur.oitava.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
