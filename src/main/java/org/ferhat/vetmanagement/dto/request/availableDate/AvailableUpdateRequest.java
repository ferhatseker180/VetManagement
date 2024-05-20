package org.ferhat.vetmanagement.dto.request.availableDate;

import lombok.Data;
import org.ferhat.vetmanagement.entities.Doctor;

import java.time.LocalDate;

@Data
public class AvailableUpdateRequest {

    private Long id;
    private LocalDate availableDate;
    private Doctor doctor;
}
