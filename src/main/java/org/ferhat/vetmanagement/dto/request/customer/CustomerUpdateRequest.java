package org.ferhat.vetmanagement.dto.request.customer;

import lombok.Data;
import java.util.List;

@Data
public class CustomerUpdateRequest {

    private Long id;
    private String name;
    private String phone;
    private String mail;
    private String address;
    private String city;
    private List<Long> animalIdList;
}
