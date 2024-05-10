package org.ferhat.vetmanagement.business.abstracts;

import org.ferhat.vetmanagement.entities.Animal;
import org.ferhat.vetmanagement.entities.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICustomerService {

    Customer save(Customer customer);

    Customer update(Customer customer);

    boolean delete(Long id);

    Customer get(Long id);

    Page<Customer> cursor(int page, int pageSize);

    List<Customer> findCustomersByNameIgnoreCase(String name);

    List<Animal> getAnimals(List<Long> idList);

    List<Animal> getCustomerAnimals(Long customerId);
}
