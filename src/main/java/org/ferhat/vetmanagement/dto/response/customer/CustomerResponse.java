package org.ferhat.vetmanagement.dto.response.customer;

import lombok.Data;
import org.ferhat.vetmanagement.dto.response.animal.AnimalResponse;
import org.ferhat.vetmanagement.entities.Animal;

import java.util.List;

@Data
public class CustomerResponse {

    private Long id;
    private String name;
    private String phone;
    private String mail;
    private String address;
    private String city;
    private List<AnimalResponse> animalList;

}
