package org.ferhat.vetmanagement.business.impl;

import org.ferhat.vetmanagement.business.abstracts.IAppointment;
import org.ferhat.vetmanagement.business.abstracts.IAvailableDateService;
import org.ferhat.vetmanagement.core.exceptions.NotFoundException;
import org.ferhat.vetmanagement.core.utils.Msg;
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

@Service
public class AppointmentManager implements IAppointment {
    private final AppointmentRepo appointmentRepo;
    private final IAvailableDateService availableDateService;

    public AppointmentManager(AppointmentRepo appointmentRepo, IAvailableDateService availableDateService) {
        this.appointmentRepo = appointmentRepo;
        this.availableDateService = availableDateService;
    }

    @Override
    public Appointment save(Appointment appointment) {

        // Doktorun belirtilen tarihte müsait olup olmadığını kontrol et
        List<AvailableDate> availableDates = availableDateService.findByDoctorIdAndAvailableDate(appointment.getDoctor().getId(), appointment.getAppointmentDate().toLocalDate());
        if (availableDates.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Doctor is not available on the specified date");
        }

        // Doktorun belirtilen saatte randevusu var mı kontrol et
        if (!isDoctorAvailableAtHour(appointment.getAppointmentDate(), appointment.getDoctor().getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Doctor has another appointment at the specified hour");
        }

        return this.appointmentRepo.save(appointment);
    }

    @Override
    public Appointment update(Appointment appointment) {
        this.get(appointment.getId());
        return this.appointmentRepo.save(appointment);
    }

    @Override
    public boolean delete(Long id) {
        Appointment appointment = this.get(id);
        this.appointmentRepo.delete(appointment);
        return true;
    }

    @Override
    public Appointment get(Long id) {
        return this.appointmentRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
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
}
