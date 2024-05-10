package org.ferhat.vetmanagement.dto.request.customer;

import lombok.Data;

@Data
public class CustomerSaveRequest {
    private String name;
    private String phone;
    private String mail;
    private String address;
    private String city;
}
