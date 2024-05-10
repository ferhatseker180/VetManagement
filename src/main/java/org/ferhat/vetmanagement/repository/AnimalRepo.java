package org.ferhat.vetmanagement.repository;

import org.ferhat.vetmanagement.entities.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalRepo extends JpaRepository<Animal, Long> {
    List<Animal> findAnimalsByNameIgnoreCase(String name);
}
