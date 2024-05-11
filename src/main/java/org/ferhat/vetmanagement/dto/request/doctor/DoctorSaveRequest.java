package org.ferhat.vetmanagement.dto.request.doctor;

import lombok.Data;

@Data
public class DoctorSaveRequest {

    private String name;
    private String phone;
    private String mail;
    private String address;
    private String city;

}
