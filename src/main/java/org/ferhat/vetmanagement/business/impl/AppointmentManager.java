package org.ferhat.vetmanagement.business.impl;

import org.ferhat.vetmanagement.business.abstracts.IAppointment;
import org.ferhat.vetmanagement.business.abstracts.IAvailableDateService;
import org.ferhat.vetmanagement.core.config.modelMapper.IModelMapperService;
import org.ferhat.vetmanagement.core.exceptions.NotFoundException;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.appointment.AppointmentMessage;
import org.ferhat.vetmanagement.core.utils.appointment.AppointmentResultHelper;
import org.ferhat.vetmanagement.dto.request.appointment.AppointmentSaveRequest;
import org.ferhat.vetmanagement.dto.request.appointment.AppointmentUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.appointment.AppointmentResponse;
import org.ferhat.vetmanagement.entities.Appointment;
import org.ferhat.vetmanagement.entities.AvailableDate;
import org.ferhat.vetmanagement.repository.AppointmentRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentManager implements IAppointment {
    private final AppointmentRepo appointmentRepo;
    private final IAvailableDateService availableDateService;
    private final IModelMapperService modelMapperService;

    public AppointmentManager(AppointmentRepo appointmentRepo, IAvailableDateService availableDateService, IModelMapperService modelMapperService) {
        this.appointmentRepo = appointmentRepo;
        this.availableDateService = availableDateService;
        this.modelMapperService = modelMapperService;
    }

    // Save Appointment
    @Override
    public ResultData<AppointmentResponse> save(AppointmentSaveRequest appointmentSaveRequest) {

        Appointment saveAppointment = modelMapperService.forRequest().map(appointmentSaveRequest, Appointment.class);

        // Check if the doctor is available on the specified date
        List<AvailableDate> availableDates = availableDateService.findByDoctorIdAndAvailableDate(saveAppointment.getDoctor().getId(), saveAppointment.getAppointmentDate().toLocalDate());
        if (availableDates.isEmpty()) {
            throw new NotFoundException(AppointmentMessage.NOT_FOUND);
        }

        // Check if the doctor has an appointment at the specified time
        if (!isDoctorAvailableAtHour(saveAppointment.getAppointmentDate(), saveAppointment.getDoctor().getId())) {
            throw new NotFoundException(AppointmentMessage.FULL_APPOINTMENT);
        }

        // Check if the new appointment is 1 hour earlier or 1 hour later
        LocalDateTime startRange = saveAppointment.getAppointmentDate().minusHours(1);
        LocalDateTime endRange = saveAppointment.getAppointmentDate().plusHours(1);
        List<Appointment> appointmentsInTimeRange = appointmentRepo.findByDoctorIdAndAppointmentDateBetween(saveAppointment.getDoctor().getId(), startRange, endRange);
        if (!appointmentsInTimeRange.isEmpty()) {
            throw new NotFoundException(AppointmentMessage.BUSY_APPOINTMENT);
        }

        Appointment savedAppointment = this.appointmentRepo.save(saveAppointment);
        AppointmentResponse appointmentResponse = modelMapperService.forResponse().map(savedAppointment, AppointmentResponse.class);
        return AppointmentResultHelper.created(appointmentResponse);
    }


    // Update Appointment
    @Override
    public AppointmentResponse update(AppointmentUpdateRequest appointmentUpdateRequest) {
        // AppointmentUpdateRequest nesnesini Appointment nesnesine dönüştür
        Appointment updateAppointment = this.modelMapperService.forRequest().map(appointmentUpdateRequest, Appointment.class);

        // Var olan randevunun olup olmadığını kontrol et
        this.appointmentRepo.findById(updateAppointment.getId())
                    .orElseThrow(() -> new NotFoundException(AppointmentMessage.NOT_FOUND));

        // Güncellenmiş randevuyu veri tabanına kaydet
        Appointment updatedAppointment = this.appointmentRepo.save(updateAppointment);

        // Güncellenmiş randevuyu AppointmentResponse nesnesine dönüştür ve döndür
        return this.modelMapperService.forResponse().map(updatedAppointment, AppointmentResponse.class);
    }


    @Override
    public String delete(Long id) {
        Appointment appointment = this.appointmentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(AppointmentMessage.NOT_FOUND));
        this.appointmentRepo.delete(appointment);
        return AppointmentMessage.DELETED;
    }

    @Override
    public AppointmentResponse get(Long id) {
        Appointment appointment = this.appointmentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(AppointmentMessage.NOT_FOUND));
        return this.modelMapperService.forResponse().map(appointment, AppointmentResponse.class);
    }

    @Override
    public ResultData<CursorResponse<AppointmentResponse>> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Appointment> appointmentPage = this.appointmentRepo.findAll(pageable);
        Page<AppointmentResponse> appointmentResponsePage = appointmentPage
                .map(appointment -> this.modelMapperService.forResponse().map(appointment, AppointmentResponse.class));
        return AppointmentResultHelper.cursor(appointmentResponsePage);
    }

    @Override
    public List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDateTime appointmentDate) {
        return this.appointmentRepo.findByDoctorIdAndAppointmentDate(doctorId, appointmentDate);
    }

    @Override
    public boolean isDoctorAvailableAtHour(LocalDateTime hour, Long doctorId) {
        List<Appointment> appointmentList = appointmentRepo.findByDoctorIdAndAppointmentDate(doctorId, hour);
        return appointmentList.isEmpty();
    }

    @Override
    public List<AppointmentResponse> findByAppointmentDateBetweenAndAnimalId(LocalDateTime startDate, LocalDateTime endDate, Long animalId) {
        List<Appointment> appointments = appointmentRepo.findByAppointmentDateBetweenAndAnimalId(startDate, endDate, animalId);
        return appointments.stream()
                .map(appointment -> modelMapperService.forResponse().map(appointment, AppointmentResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> findByAppointmentDateBetweenAndDoctorId(LocalDateTime startDate, LocalDateTime endDate, Long doctorId) {
        List<Appointment> appointments = appointmentRepo.findByAppointmentDateBetweenAndDoctorId(startDate, endDate, doctorId);
        return appointments.stream()
                .map(appointment -> modelMapperService.forResponse().map(appointment, AppointmentResponse.class))
                .collect(Collectors.toList());
    }
}
