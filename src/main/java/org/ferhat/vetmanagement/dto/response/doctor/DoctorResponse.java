package org.ferhat.vetmanagement.dto.response.doctor;

import lombok.Data;
import org.ferhat.vetmanagement.entities.AvailableDate;

import java.util.List;

@Data
public class DoctorResponse {

    private Long id;
    private String name;
    private String phone;
    private String mail;
    private String address;
    private String city;
    private List<AvailableDate> availableDateList;
}
