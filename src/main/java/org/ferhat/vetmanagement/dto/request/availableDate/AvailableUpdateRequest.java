package org.ferhat.vetmanagement.dto.request.availableDate;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AvailableUpdateRequest {

    private Long id;
    private LocalDate availableDate;
    private Long doctorId;
}
