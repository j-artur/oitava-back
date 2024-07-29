package dev.jartur.oitava.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jartur.oitava.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
