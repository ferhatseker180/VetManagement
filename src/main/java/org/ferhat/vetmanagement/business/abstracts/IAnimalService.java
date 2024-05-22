package org.ferhat.vetmanagement.business.abstracts;

import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.dto.request.animal.AnimalSaveRequest;
import org.ferhat.vetmanagement.dto.request.animal.AnimalUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.animal.AnimalResponse;
import org.ferhat.vetmanagement.entities.Animal;
import org.ferhat.vetmanagement.entities.Customer;

import java.util.List;

public interface IAnimalService {

    ResultData<AnimalResponse> save(AnimalSaveRequest animalSaveRequest);

    Animal update(Animal animal);

    AnimalResponse updateAndReturnResponse(AnimalUpdateRequest animalUpdateRequest);

    String delete(Long id);

    Animal get(Long id);

    AnimalResponse getAnimalResponseById(Long id);

    List<Animal> getAll();

    ResultData<CursorResponse<AnimalResponse>> cursor(int page, int pageSize);

    Customer getCustomer(Long id);

    List<AnimalResponse> findAnimalsByNameIgnoreCase(String name);
}
