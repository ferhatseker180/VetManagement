package org.ferhat.vetmanagement.business.impl;

import org.ferhat.vetmanagement.business.abstracts.ICustomerService;
import org.ferhat.vetmanagement.core.config.modelMapper.IModelMapperService;
import org.ferhat.vetmanagement.core.exceptions.NotFoundException;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.animal.AnimalMessage;
import org.ferhat.vetmanagement.core.utils.customer.CustomerMessage;
import org.ferhat.vetmanagement.core.utils.customer.CustomerResultHelper;
import org.ferhat.vetmanagement.dto.request.customer.CustomerSaveRequest;
import org.ferhat.vetmanagement.dto.request.customer.CustomerUpdateRequest;
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
    public CustomerResponse save(CustomerSaveRequest customerSaveRequest) {
        Customer saveCustomer = modelMapperService.forRequest().map(customerSaveRequest, Customer.class);
        Customer savedCustomer = customerRepo.save(saveCustomer);
        return modelMapperService.forResponse().map(savedCustomer, CustomerResponse.class);
    }

    @Override
    public CustomerResponse update(CustomerUpdateRequest customerUpdateRequest) {
        Customer updateCustomer = modelMapperService.forRequest().map(customerUpdateRequest, Customer.class);
        get(updateCustomer.getId());
        Customer updatedCustomer = customerRepo.save(updateCustomer);
        return modelMapperService.forResponse().map(updatedCustomer, CustomerResponse.class);
    }

    @Override
    public String delete(Long id) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(CustomerMessage.NOT_FOUND));
        customerRepo.delete(customer);
        return CustomerMessage.DELETED;
    }

    @Override
    public CustomerResponse get(Long id) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(CustomerMessage.NOT_FOUND));
        return modelMapperService.forResponse().map(customer, CustomerResponse.class);
    }

    @Override
    public ResultData<CursorResponse<CustomerResponse>> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Customer> customerPage = this.customerRepo.findAll(pageable);
        Page<CustomerResponse> customerResponsePage = customerPage
                .map(customer -> this.modelMapperService.forResponse().map(customer, CustomerResponse.class));
        return CustomerResultHelper.cursor(customerResponsePage);
    }

    // Eliminates case sensitivity when searching by customer name
    @Override
    public List<CustomerResponse> findCustomersByNameIgnoreCase(String name) {
        String lowerName = name.toLowerCase();
        List<Customer> customers = this.customerRepo.findCustomersByNameIgnoreCase(lowerName);
        if (customers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CustomerMessage.NOT_FOUND);
        }

        return customers.stream()
                .map(customer -> modelMapperService.forResponse().map(customer, CustomerResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Animal> getAnimals(List<Long> idList) {
        return this.animalRepo.findAllById(idList);
    }

    // Listing customer's animals
    @Override
    public List<AnimalResponse> getCustomerAnimals(Long customerId) {
        Customer customer = this.customerRepo.findById(customerId)
                .orElseThrow(() -> new NotFoundException(CustomerMessage.NOT_FOUND));
        List<Animal> animals = customer.getAnimalList();
        if (animals.isEmpty()) {
            throw new NotFoundException(AnimalMessage.NOT_FOUND);
        }

        return animals.stream()
                .map(animal -> modelMapperService.forResponse().map(animal, AnimalResponse.class))
                .collect(Collectors.toList());
    }
}

