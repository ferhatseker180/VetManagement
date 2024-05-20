package org.ferhat.vetmanagement.business.impl;

import org.ferhat.vetmanagement.business.abstracts.ICustomerService;
import org.ferhat.vetmanagement.core.config.modelMapper.IModelMapperService;
import org.ferhat.vetmanagement.core.exceptions.NotFoundException;
import org.ferhat.vetmanagement.core.utils.Msg;
import org.ferhat.vetmanagement.dto.response.animal.AnimalResponse;
import org.ferhat.vetmanagement.dto.response.customer.CustomerResponse;
import org.ferhat.vetmanagement.entities.Animal;
import org.ferhat.vetmanagement.entities.Customer;
import org.ferhat.vetmanagement.repository.AnimalRepo;
import org.ferhat.vetmanagement.repository.CustomerRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerManager implements ICustomerService {
    private final CustomerRepo customerRepo;
    private final AnimalRepo animalRepo;
    private final IModelMapperService modelMapperService;

    public CustomerManager(CustomerRepo customerRepo, AnimalRepo animalRepo, IModelMapperService modelMapperService) {
        this.customerRepo = customerRepo;
        this.animalRepo = animalRepo;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public Customer save(Customer customer) {
        return this.customerRepo.save(customer);
    }

    @Override
    public Customer update(Customer customer) {
        this.get(customer.getId());
        return this.customerRepo.save(customer);
    }

    @Override
    public boolean delete(Long id) {
        Customer customer = this.get(id);
        this.customerRepo.delete(customer);
        return true;
    }

    @Override
    public Customer get(Long id) {
        return this.customerRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Page<Customer> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.customerRepo.findAll(pageable);
    }

    @Override
    public List<CustomerResponse> findCustomersByNameIgnoreCase(String name) {
        String lowerName = name.toLowerCase();
        List<Customer> customers = this.customerRepo.findCustomersByNameIgnoreCase(lowerName);
        if (customers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }

        return customers.stream()
                .map(customer -> modelMapperService.forResponse().map(customer, CustomerResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Animal> getAnimals(List<Long> idList) {
        return this.animalRepo.findAllById(idList);
    }

    @Override
    public List<AnimalResponse> getCustomerAnimals(Long customerId) {
        Customer customer = get(customerId);
        List<Animal> animals = customer.getAnimalList();
        if (animals.isEmpty()) {
            throw new NotFoundException("Not found animal");
        }

        return animals.stream()
                .map(animal -> modelMapperService.forResponse().map(animal, AnimalResponse.class))
                .collect(Collectors.toList());
    }
}

