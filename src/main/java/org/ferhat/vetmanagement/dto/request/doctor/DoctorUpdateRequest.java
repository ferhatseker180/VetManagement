package org.ferhat.vetmanagement.dto.request.doctor;

import lombok.Data;
import java.util.List;

@Data
public class DoctorUpdateRequest {

    private Long id;
    private String name;
    private String phone;
    private String mail;
    private String address;
    private String city;
    //  private List<Long> avaliableIdList;
    //  private List<Long> appointmentIdList;
}
