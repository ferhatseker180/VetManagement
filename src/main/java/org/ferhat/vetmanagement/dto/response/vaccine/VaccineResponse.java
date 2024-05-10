package org.ferhat.vetmanagement.dto.response.vaccine;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VaccineResponse {
    private Long id;
    private String name;
    private String code;
    private LocalDate protectionStartDate;
    private LocalDate protectionFinishDate;
    private Long animalId;
}
