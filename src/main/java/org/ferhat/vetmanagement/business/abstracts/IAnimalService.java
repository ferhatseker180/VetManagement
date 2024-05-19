package org.ferhat.vetmanagement.business.abstracts;

import org.ferhat.vetmanagement.dto.request.animal.AnimalSaveRequest;
import org.ferhat.vetmanagement.dto.request.animal.AnimalUpdateRequest;
import org.ferhat.vetmanagement.dto.response.animal.AnimalResponse;
import org.ferhat.vetmanagement.entities.Animal;
import org.ferhat.vetmanagement.entities.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAnimalService {

    AnimalResponse save(AnimalSaveRequest animalSaveRequest);

    Animal update(Animal animal);

    AnimalResponse updateAndReturnResponse(AnimalUpdateRequest animalUpdateRequest);

    boolean delete(Long id);

    Animal get(Long id);

    AnimalResponse getAnimalResponseById(Long id);

    List<Animal> getAll();

    Page<Animal> cursor(int page, int pageSize);

    Customer getCustomer(Long id);

    List<AnimalResponse> findAnimalsByNameIgnoreCase(String name);
}
