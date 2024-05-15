package org.ferhat.vetmanagement.dto.response.appointment;

import lombok.Data;
import org.ferhat.vetmanagement.dto.response.animal.AnimalResponse;
import org.ferhat.vetmanagement.dto.response.doctor.DoctorResponse;

import java.time.LocalDateTime;

@Data
public class AppointmentResponse {
    private Long id;
    private LocalDateTime appointmentDate;
    private DoctorResponse doctor;
    private AnimalResponse animal;
}
