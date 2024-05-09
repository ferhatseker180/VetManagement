package org.ferhat.vetmanagement.repository;

import org.ferhat.vetmanagement.entities.AvailableDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableDateRepo extends JpaRepository<AvailableDate, Long> {
}
