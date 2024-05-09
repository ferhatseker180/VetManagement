package org.ferhat.vetmanagement.business.abstracts;

import org.ferhat.vetmanagement.entities.Animal;
import org.springframework.data.domain.Page;

public interface IAnimalService {

    Animal save(Animal animal);

    Animal update(Animal animal);

    boolean delete(Long id);

    Animal get(Long id);

    Page<Animal> cursor(int page, int pageSize);
}
