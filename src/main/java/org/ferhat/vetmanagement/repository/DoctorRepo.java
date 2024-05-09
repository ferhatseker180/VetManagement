package org.ferhat.vetmanagement.repository;

import org.ferhat.vetmanagement.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepo extends JpaRepository<Doctor, Long> {
}
