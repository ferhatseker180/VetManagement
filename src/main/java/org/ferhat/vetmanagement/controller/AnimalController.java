package org.ferhat.vetmanagement.controller;

import jakarta.validation.Valid;
import org.ferhat.vetmanagement.business.abstracts.IAnimalService;
import org.ferhat.vetmanagement.business.abstracts.ICustomerService;
import org.ferhat.vetmanagement.core.config.modelMapper.IModelMapperService;
import org.ferhat.vetmanagement.core.result.Result;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.ResultHelper;
import org.ferhat.vetmanagement.dto.request.animal.AnimalSaveRequest;
import org.ferhat.vetmanagement.dto.request.animal.AnimalUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.animal.AnimalResponse;
import org.ferhat.vetmanagement.entities.Animal;
import org.ferhat.vetmanagement.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/animals")
public class AnimalController {
    private final IAnimalService animalService;
    private final IModelMapperService modelMapperService;
    private final ICustomerService customerService;

    public AnimalController(IAnimalService animalService, IModelMapperService modelMapperService, ICustomerService customerService) {
        this.animalService = animalService;
        this.modelMapperService = modelMapperService;
        this.customerService = customerService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AnimalResponse> save(@Valid @RequestBody AnimalSaveRequest animalSaveRequest) {
        // Customer ID'yi al
        Long customerId = animalSaveRequest.getCustomerId();

        // Veritabanında bu müşteri ID'siyle ilgili müşteriyi kontrol et
        Customer customer = customerService.get(customerId);
        if (customer == null) {
            // Eğer müşteri bulunamazsa uygun bir hata işle
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }
        Animal saveAnimal = modelMapperService.forRequest().map(animalSaveRequest, Animal.class);

        saveAnimal.setCustomer(customer);
        Animal savedAnimal = animalService.save(saveAnimal);
        AnimalResponse animalResponse = modelMapperService.forResponse().map(savedAnimal, AnimalResponse.class);
        return ResultHelper.created(animalResponse);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> get(@PathVariable("id") Long id) {
        Animal animal = this.animalService.get(id);
        AnimalResponse animalResponse = this.modelMapperService.forResponse().map(animal, AnimalResponse.class);
        return ResultHelper.success(animalResponse);
    }

    // Customer isme göre arama
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> findAnimalsByNameIgnoreCase(@RequestParam("name") String name) {
        List<Animal> animals = this.animalService.findAnimalsByNameIgnoreCase(name);

        if (animals.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal not found");
        }

        List<AnimalResponse> animalResponses = new ArrayList<>();

        for (Animal animal : animals) {
            animalResponses.add(modelMapperService.forResponse().map(animal, AnimalResponse.class));
        }
        return ResultHelper.success(animalResponses);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AnimalResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {

        Page<Animal> animalPage = this.animalService.cursor(page, pageSize);
        Page<AnimalResponse> animalResponsePage = animalPage
                .map(animal -> this.modelMapperService.forResponse().map(animal, AnimalResponse.class));

        return ResultHelper.cursor(animalResponsePage);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> update(@Valid @RequestBody AnimalUpdateRequest animalUpdateRequest) {
        Animal updateAnimal = this.modelMapperService.forRequest().map(animalUpdateRequest, Animal.class);
        this.animalService.update(updateAnimal);
        return ResultHelper.success(this.modelMapperService.forResponse().map(updateAnimal, AnimalResponse.class));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        this.animalService.delete(id);
        return ResultHelper.ok();
    }

}
