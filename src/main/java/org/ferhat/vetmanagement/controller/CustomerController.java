package org.ferhat.vetmanagement.controller;

import jakarta.validation.Valid;
import org.ferhat.vetmanagement.business.abstracts.ICustomerService;
import org.ferhat.vetmanagement.core.result.Result;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.ResultHelper;
import org.ferhat.vetmanagement.dto.request.customer.CustomerSaveRequest;
import org.ferhat.vetmanagement.dto.request.customer.CustomerUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.animal.AnimalResponse;
import org.ferhat.vetmanagement.dto.response.customer.CustomerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {
    private final ICustomerService customerService;

    public CustomerController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<CustomerResponse> save(@Valid @RequestBody CustomerSaveRequest customerSaveRequest) {
        CustomerResponse response = customerService.save(customerSaveRequest);
        return ResultHelper.created(response);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CustomerResponse> get(@PathVariable("id") Long id) {
        CustomerResponse customerResponse = customerService.get(id);
        return ResultHelper.success(customerResponse);
    }

    // Customer isme göre arama
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<CustomerResponse>> findCustomersByName(@RequestParam("name") String name) {
        List<CustomerResponse> customerResponses = customerService.findCustomersByNameIgnoreCase(name);
        return ResultHelper.success(customerResponses);
    }

    // Show all animals belonging to customer
    @GetMapping("/{customerId}/animals")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> getCustomerAnimals(@PathVariable("customerId") Long customerId) {
        List<AnimalResponse> animalResponses = customerService.getCustomerAnimals(customerId);
        return ResultHelper.success(animalResponses);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<CustomerResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {

        return this.customerService.cursor(page, pageSize);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CustomerResponse> update(@Valid @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        CustomerResponse response = customerService.update(customerUpdateRequest);
        return ResultHelper.success(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        this.customerService.delete(id);
        return ResultHelper.ok();
    }

}
