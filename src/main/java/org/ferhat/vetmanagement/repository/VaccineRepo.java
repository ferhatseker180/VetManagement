package org.ferhat.vetmanagement.repository;

import org.ferhat.vetmanagement.entities.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccineRepo extends JpaRepository<Vaccine, Long> {
}
