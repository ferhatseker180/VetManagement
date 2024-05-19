package org.ferhat.vetmanagement.business.impl;

import org.ferhat.vetmanagement.business.abstracts.IAnimalService;
import org.ferhat.vetmanagement.business.abstracts.ICustomerService;
import org.ferhat.vetmanagement.core.config.modelMapper.IModelMapperService;
import org.ferhat.vetmanagement.core.exceptions.NotFoundException;
import org.ferhat.vetmanagement.core.utils.Msg;
import org.ferhat.vetmanagement.dto.request.animal.AnimalSaveRequest;
import org.ferhat.vetmanagement.dto.request.animal.AnimalUpdateRequest;
import org.ferhat.vetmanagement.dto.response.animal.AnimalResponse;
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

    @Override
    @Transactional
    public AnimalResponse save(AnimalSaveRequest animalSaveRequest) {
        Animal saveAnimal = modelMapperService.forRequest().map(animalSaveRequest, Animal.class);
        Long customerId = saveAnimal.getCustomer().getId();
        Customer customer = customerService.get(customerId);
        if (customer == null) {
            throw new NotFoundException(Msg.NOT_FOUND);
        }
        saveAnimal.setCustomer(customer);
        Animal savedAnimal = this.animalRepo.save(saveAnimal);
        return modelMapperService.forResponse().map(savedAnimal, AnimalResponse.class);
    }


    @Override
    public Animal update(Animal animal) {
        this.get(animal.getId());
        return this.animalRepo.save(animal);
    }

    @Override
    public AnimalResponse updateAndReturnResponse(AnimalUpdateRequest animalUpdateRequest) {
        Animal updateAnimal = this.modelMapperService.forRequest().map(animalUpdateRequest, Animal.class);
        Animal updatedAnimal = this.update(updateAnimal);
        return this.modelMapperService.forResponse().map(updatedAnimal, AnimalResponse.class);
    }


    @Override
    public boolean delete(Long id) {
        Animal animal = this.get(id);
        this.animalRepo.delete(animal);
        return true;
    }

    @Override
    public Animal get(Long id) {
        return this.animalRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public AnimalResponse getAnimalResponseById(Long id) {
        Animal animal = get(id);
        return modelMapperService.forResponse().map(animal, AnimalResponse.class);
    }

    @Override
    public List<Animal> getAll() {
        return this.animalRepo.findAll();
    }

    @Override
    public Page<Animal> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.animalRepo.findAll(pageable);
    }

    @Override
    public Customer getCustomer(Long id) {
        return this.customerRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public List<AnimalResponse> findAnimalsByNameIgnoreCase(String name) {
        List<Animal> animals = this.animalRepo.findAnimalsByNameIgnoreCase(name);
        if (animals.isEmpty()) {
            throw new NotFoundException("Animal not found");
        }
        return animals.stream()
                .map(animal -> modelMapperService.forResponse().map(animal, AnimalResponse.class))
                .collect(Collectors.toList());
    }
}
