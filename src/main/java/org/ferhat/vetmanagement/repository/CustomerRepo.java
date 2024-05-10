package org.ferhat.vetmanagement.repository;

import org.ferhat.vetmanagement.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    List<Customer> findCustomersByNameIgnoreCase(String name);
}
