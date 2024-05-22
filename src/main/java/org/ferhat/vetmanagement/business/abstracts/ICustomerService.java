package org.ferhat.vetmanagement.business.abstracts;

import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.dto.request.customer.CustomerSaveRequest;
import org.ferhat.vetmanagement.dto.request.customer.CustomerUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.animal.AnimalResponse;
import org.ferhat.vetmanagement.dto.response.customer.CustomerResponse;
import org.ferhat.vetmanagement.entities.Animal;


import java.util.List;

public interface ICustomerService {

    CustomerResponse save(CustomerSaveRequest customerSaveRequest);

    CustomerResponse update(CustomerUpdateRequest customerUpdateRequest);

    String delete(Long id);

    CustomerResponse get(Long id);

    ResultData<CursorResponse<CustomerResponse>> cursor(int page, int pageSize);

    List<CustomerResponse> findCustomersByNameIgnoreCase(String name);

    List<Animal> getAnimals(List<Long> idList);

    List<AnimalResponse> getCustomerAnimals(Long customerId);
}
