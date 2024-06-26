package org.ferhat.vetmanagement.dto.request.animal;

import lombok.Data;
import org.ferhat.vetmanagement.entities.Customer;

import java.time.LocalDate;

@Data
public class AnimalSaveRequest {

    private String name;
    private String species;
    private String breed;
    private String gender;
    private LocalDate dateOfBirth;
    private String color;
    private Customer customer;
}
