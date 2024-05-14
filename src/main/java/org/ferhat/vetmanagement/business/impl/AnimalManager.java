package org.ferhat.vetmanagement.business.impl;

import org.ferhat.vetmanagement.business.abstracts.IAnimalService;
import org.ferhat.vetmanagement.business.abstracts.ICustomerService;
import org.ferhat.vetmanagement.core.exceptions.NotFoundException;
import org.ferhat.vetmanagement.core.utils.Msg;
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

@Service
public class AnimalManager implements IAnimalService {
    private final AnimalRepo animalRepo;
    private final CustomerRepo customerRepo;
    private final ICustomerService customerService;

    public AnimalManager(AnimalRepo animalRepo, CustomerRepo customerRepo, ICustomerService customerService) {
        this.animalRepo = animalRepo;
        this.customerRepo = customerRepo;
        this.customerService = customerService;
    }

    @Override
    @Transactional
    public Animal save(Animal animal) {
        Long customerId = animal.getCustomer().getId();
        Customer customer = customerService.get(customerId);
        if (customer == null) {
            throw new NotFoundException(Msg.NOT_FOUND);
        }
        animal.setCustomer(customer);
        return this.animalRepo.save(animal);
    }

    @Override
    public Animal update(Animal animal) {
        this.get(animal.getId());
        return this.animalRepo.save(animal);
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
    public List<Animal> findAnimalsByNameIgnoreCase(String name) {
        return this.animalRepo.findAnimalsByNameIgnoreCase(name);
    }
}
