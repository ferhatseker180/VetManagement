package org.ferhat.vetmanagement.repository;

import org.ferhat.vetmanagement.entities.AvailableDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AvailableDateRepo extends JpaRepository<AvailableDate, Long> {

    List<AvailableDate> findByDoctorIdAndAvailableDate(Long doctorId, LocalDate date);
}
