package org.ferhat.vetmanagement.business.abstracts;

import org.ferhat.vetmanagement.entities.Appointment;
import org.ferhat.vetmanagement.entities.AvailableDate;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IAppointment {

    Appointment save(Appointment appointment);

    Appointment update(Appointment appointment);

    boolean delete(Long id);

    Appointment get(Long id);

    Page<Appointment> cursor(int page, int pageSize);

    List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDateTime appointmentDate);

    boolean isDoctorAvailableAtHour(LocalDateTime hour, Long doctorId);


}
