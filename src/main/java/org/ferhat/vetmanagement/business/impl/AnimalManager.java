package org.ferhat.vetmanagement.business.impl;

import org.ferhat.vetmanagement.business.abstracts.IAnimalService;
import org.ferhat.vetmanagement.business.abstracts.ICustomerService;
import org.ferhat.vetmanagement.core.config.modelMapper.IModelMapperService;
import org.ferhat.vetmanagement.core.exceptions.NotFoundException;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.animal.AnimalMessage;
import org.ferhat.vetmanagement.core.utils.ResultHelper;
import org.ferhat.vetmanagement.core.utils.animal.AnimalResultHelper;
import org.ferhat.vetmanagement.dto.request.animal.AnimalSaveRequest;
import org.ferhat.vetmanagement.dto.request.animal.AnimalUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.animal.AnimalResponse;
import org.ferhat.vetmanagement.dto.response.customer.CustomerResponse;
import org.ferhat.vetmanagement.entities.Animal;
import org.ferhat.vetmanagement.entities.Customer;
import org.ferhat.vetmanagement.repository.AnimalRepo;
import org.ferhat.vetmanagement.repository.CustomerRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalManager implements IAnimalService {
    private final AnimalRepo animalRepo;
    private final CustomerRepo customerRepo;
    private final ICustomerService customerService;
    private final IModelMapperService modelMapperService;

    public AnimalManager(AnimalRepo animalRepo, CustomerRepo customerRepo, ICustomerService customerService, IModelMapperService modelMapperService) {
        this.animalRepo = animalRepo;
        this.customerRepo = customerRepo;
        this.customerService = customerService;
        this.modelMapperService = modelMapperService;
    }

    // Add Animal
    @Override
    @Transactional
    public ResultData<AnimalResponse> save(AnimalSaveRequest animalSaveRequest) {
        Animal saveAnimal = modelMapperService.forRequest().map(animalSaveRequest, Animal.class);
        Long customerId = saveAnimal.getCustomer().getId();
        CustomerResponse customerResponse = customerService.get(customerId);
        if (customerResponse == null) {
            throw new NotFoundException(AnimalMessage.NOT_FOUND);
        }
        saveAnimal.setCustomer(modelMapperService.forRequest().map(customerResponse, Customer.class));
        Animal savedAnimal = this.animalRepo.save(saveAnimal);
        AnimalResponse animalResponse = modelMapperService.forResponse().map(savedAnimal, AnimalResponse.class);
        return AnimalResultHelper.created(animalResponse);
    }


    // Update Animal
    @Override
    public Animal update(Animal animal) {
        this.get(animal.getId());
        return this.animalRepo.save(animal);
    }

    // Convert request to response or response to request
    @Override
    public AnimalResponse updateAndReturnResponse(AnimalUpdateRequest animalUpdateRequest) {
        Animal updateAnimal = this.modelMapperService.forRequest().map(animalUpdateRequest, Animal.class);
        Animal updatedAnimal = this.update(updateAnimal);
        return this.modelMapperService.forResponse().map(updatedAnimal, AnimalResponse.class);
    }

    // Delete animal
    @Override
    public String delete(Long id) {
        Animal animal = this.get(id);
        this.animalRepo.delete(animal);
        return AnimalMessage.DELETED;
    }

    // Bring animal by the id number
    @Override
    public Animal get(Long id) {
        return this.animalRepo.findById(id).orElseThrow(() -> new NotFoundException(AnimalMessage.NOT_FOUND));
    }

    // Convert to Response for get Animal
    @Override
    public AnimalResponse getAnimalResponseById(Long id) {
        Animal animal = get(id);
        return modelMapperService.forResponse().map(animal, AnimalResponse.class);
    }

    // Bring All Animals
    @Override
    public List<Animal> getAll() {
        return this.animalRepo.findAll();
    }

    @Override
    public ResultData<CursorResponse<AnimalResponse>> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Animal> animalPage = this.animalRepo.findAll(pageable);
        Page<AnimalResponse> animalResponsePage = animalPage
                .map(animal -> this.modelMapperService.forResponse().map(animal, AnimalResponse.class));
        return AnimalResultHelper.cursor(animalResponsePage);
    }

    // Bring Customer
    @Override
    public Customer getCustomer(Long id) {
        return this.customerRepo.findById(id).orElseThrow(() -> new NotFoundException(AnimalMessage.NOT_FOUND));
    }

    // Filter and fetch animals by name
    @Override
    public List<AnimalResponse> findAnimalsByNameIgnoreCase(String name) {
        List<Animal> animals = this.animalRepo.findAnimalsByNameIgnoreCase(name);
        if (animals.isEmpty()) {
            throw new NotFoundException(AnimalMessage.NOT_FOUND);
        }
        return animals.stream()
                .map(animal -> modelMapperService.forResponse().map(animal, AnimalResponse.class))
                .collect(Collectors.toList());
    }
}
