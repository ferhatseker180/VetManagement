package org.ferhat.vetmanagement.repository;

import org.ferhat.vetmanagement.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId,LocalDateTime appointmentDate);

    List<Appointment> findByDoctorIdAndAppointmentDateBetween(Long doctorId, LocalDateTime start, LocalDateTime end);

    // appointment search by entered date range and animal
    List<Appointment> findByAppointmentDateBetweenAndAnimalId (LocalDateTime startDate, LocalDateTime endDate, Long animalId);

    // appointment search by entered date range and doctor
    List<Appointment> findByAppointmentDateBetweenAndDoctorId (LocalDateTime startDate, LocalDateTime endDate, Long doctorId);

}
