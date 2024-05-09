package org.ferhat.vetmanagement.repository;

import org.ferhat.vetmanagement.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
}
