package org.ferhat.vetmanagement.repository;

import org.ferhat.vetmanagement.entities.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepo extends JpaRepository<Animal, Long> {
}
