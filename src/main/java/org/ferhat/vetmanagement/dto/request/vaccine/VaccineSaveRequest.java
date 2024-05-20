package org.ferhat.vetmanagement.dto.request.vaccine;

import lombok.Data;
import org.ferhat.vetmanagement.entities.Animal;

import java.time.LocalDate;

@Data
public class VaccineSaveRequest {
    private String name;
    private String code;
    private LocalDate protectionStartDate;
    private LocalDate protectionFinishDate;
    private Animal animal;
}
