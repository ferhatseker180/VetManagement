package org.ferhat.vetmanagement.dto.request.appointment;

import lombok.Data;
import org.ferhat.vetmanagement.entities.Animal;
import org.ferhat.vetmanagement.entities.Doctor;

import java.time.LocalDateTime;

@Data
public class AppointmentUpdateRequest {

    private Long id;
    private LocalDateTime appointmentDate;
    private Doctor doctor;
    private Animal animal;
}
