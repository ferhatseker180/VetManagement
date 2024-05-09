package org.ferhat.vetmanagement.repository;

import org.ferhat.vetmanagement.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
}
