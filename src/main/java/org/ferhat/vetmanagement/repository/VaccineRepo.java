package org.ferhat.vetmanagement.repository;

import org.ferhat.vetmanagement.entities.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VaccineRepo extends JpaRepository<Vaccine, Long> {

    List<Vaccine> getByAnimalId(Long animalId);

    List<Vaccine> findByProtectionStartDateBetween(LocalDate startDate, LocalDate endDate);
}
