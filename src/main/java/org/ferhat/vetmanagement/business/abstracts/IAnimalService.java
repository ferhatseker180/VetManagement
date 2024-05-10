package org.ferhat.vetmanagement.business.abstracts;

import org.ferhat.vetmanagement.entities.Animal;
import org.ferhat.vetmanagement.entities.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAnimalService {

    Animal save(Animal animal);

    Animal update(Animal animal);

    boolean delete(Long id);

    Animal get(Long id);

    List<Animal> getAll();

    Page<Animal> cursor(int page, int pageSize);

    Customer getCustomer(Long id);

    List<Animal> findAnimalsByNameIgnoreCase(String name);
}
