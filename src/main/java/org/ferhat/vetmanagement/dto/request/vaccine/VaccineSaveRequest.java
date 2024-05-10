package org.ferhat.vetmanagement.dto.request.vaccine;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VaccineSaveRequest {
    private String name;
    private String code;
    private LocalDate protectionStartDate;
    private LocalDate protectionFinishDate;
    private Long animalId;
}
