package org.ferhat.vetmanagement.dto.response.doctor;

import lombok.Data;

@Data
public class DoctorResponse {

    private Long id;
    private String name;
    private String phone;
    private String mail;
    private String address;
    private String city;
    //  private List<AvailableResponse> avaliableIdList;
    //  private List<AppointmentResponse> appointmentIdList;
}
