package org.ferhat.vetmanagement.business.impl;

import org.ferhat.vetmanagement.business.abstracts.IAppointment;
import org.ferhat.vetmanagement.business.abstracts.IAvailableDateService;
import org.ferhat.vetmanagement.core.config.modelMapper.IModelMapperService;
import org.ferhat.vetmanagement.core.exceptions.NotFoundException;
import org.ferhat.vetmanagement.core.utils.Msg;
import org.ferhat.vetmanagement.dto.request.appointment.AppointmentSaveRequest;
import org.ferhat.vetmanagement.dto.response.appointment.AppointmentResponse;
import org.ferhat.vetmanagement.entities.Appointment;
import org.ferhat.vetmanagement.entities.AvailableDate;
import org.ferhat.vetmanagement.repository.AppointmentRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Override
    public Appointment save(Appointment appointment) {

        // Check if the doctor is available on the specified date
        List<AvailableDate> availableDates = availableDateService.findByDoctorIdAndAvailableDate(appointment.getDoctor().getId(), appointment.getAppointmentDate().toLocalDate());
        if (availableDates.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Doctor is not available on the specified date");
        }

        // Check if the doctor has an appointment at the specified time
        if (!isDoctorAvailableAtHour(appointment.getAppointmentDate(), appointment.getDoctor().getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Doctor has another appointment at the specified hour");
        }

        // Check if the new appointment is 1 hour earlier or 1 hour later
        LocalDateTime startRange = appointment.getAppointmentDate().minusHours(1);
        LocalDateTime endRange = appointment.getAppointmentDate().plusHours(1);
        List<Appointment> appointmentsInTimeRange = appointmentRepo.findByDoctorIdAndAppointmentDateBetween(appointment.getDoctor().getId(), startRange, endRange);
        if (!appointmentsInTimeRange.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Another appointment is scheduled within 1 hour of this time");
        }

        return this.appointmentRepo.save(appointment);
    }

    @Override
    public AppointmentResponse saveAppointment(AppointmentSaveRequest appointmentSaveRequest) {
        Appointment saveAppointment = this.modelMapperService.forRequest().map(appointmentSaveRequest, Appointment.class);
        Appointment savedAppointment = this.save(saveAppointment);
        return modelMapperService.forResponse().map(savedAppointment, AppointmentResponse.class);
    }

    @Override
    public Appointment update(Appointment appointment) {
        this.get(appointment.getId());
        return this.appointmentRepo.save(appointment);
    }


    @Override
    public boolean delete(Long id) {
        Appointment appointment = this.appointmentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
        this.appointmentRepo.delete(appointment);
        return true;
    }

    @Override
    public AppointmentResponse get(Long id) {
        Appointment appointment = this.appointmentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
        return this.modelMapperService.forResponse().map(appointment, AppointmentResponse.class);
    }

    @Override
    public Page<Appointment> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.appointmentRepo.findAll(pageable);
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
