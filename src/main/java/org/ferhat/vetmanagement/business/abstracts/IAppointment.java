package org.ferhat.vetmanagement.business.abstracts;

import org.ferhat.vetmanagement.dto.request.appointment.AppointmentSaveRequest;
import org.ferhat.vetmanagement.dto.response.appointment.AppointmentResponse;
import org.ferhat.vetmanagement.entities.Appointment;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface IAppointment {

    Appointment save(Appointment appointment);

    AppointmentResponse saveAppointment(AppointmentSaveRequest appointmentSaveRequest);

    Appointment update(Appointment appointment);

    boolean delete(Long id);

    AppointmentResponse get(Long id);

    Page<Appointment> cursor(int page, int pageSize);

    List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDateTime appointmentDate);

    boolean isDoctorAvailableAtHour(LocalDateTime hour, Long doctorId);

    List<AppointmentResponse> findByAppointmentDateBetweenAndAnimalId(LocalDateTime startDate, LocalDateTime endDate, Long animalId);

    List<AppointmentResponse> findByAppointmentDateBetweenAndDoctorId(LocalDateTime startDate, LocalDateTime endDate, Long doctorId);


}
