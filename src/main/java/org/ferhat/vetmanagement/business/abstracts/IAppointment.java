package org.ferhat.vetmanagement.business.abstracts;

import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.dto.request.appointment.AppointmentSaveRequest;
import org.ferhat.vetmanagement.dto.request.appointment.AppointmentUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.appointment.AppointmentResponse;
import org.ferhat.vetmanagement.entities.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public interface IAppointment {

    ResultData<AppointmentResponse> save(AppointmentSaveRequest appointmentSaveRequest);

    Appointment update(Appointment appointment);

    AppointmentResponse updateAndReturnResponse(AppointmentUpdateRequest appointmentUpdateRequest);

    String delete(Long id);

    AppointmentResponse get(Long id);

    ResultData<CursorResponse<AppointmentResponse>> cursor(int page, int pageSize);

    List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDateTime appointmentDate);

    boolean isDoctorAvailableAtHour(LocalDateTime hour, Long doctorId);

    List<AppointmentResponse> findByAppointmentDateBetweenAndAnimalId(LocalDateTime startDate, LocalDateTime endDate, Long animalId);

    List<AppointmentResponse> findByAppointmentDateBetweenAndDoctorId(LocalDateTime startDate, LocalDateTime endDate, Long doctorId);


}
