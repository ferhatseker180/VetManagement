package org.ferhat.vetmanagement.dto.response.availableDate;

import lombok.Data;
import org.ferhat.vetmanagement.dto.response.doctor.DoctorResponse;

import java.time.LocalDate;

@Data
public class AvailableDateResponse {

    private Long id;
    private LocalDate availableDate;
    private DoctorResponse doctor;
}
