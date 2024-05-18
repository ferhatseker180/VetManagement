package org.ferhat.vetmanagement.controller;

import jakarta.validation.Valid;
import org.ferhat.vetmanagement.business.abstracts.IAppointment;
import org.ferhat.vetmanagement.core.config.modelMapper.IModelMapperService;
import org.ferhat.vetmanagement.core.result.Result;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.ResultHelper;
import org.ferhat.vetmanagement.dto.request.appointment.AppointmentSaveRequest;
import org.ferhat.vetmanagement.dto.request.appointment.AppointmentUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.appointment.AppointmentResponse;
import org.ferhat.vetmanagement.entities.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/appointments")
public class AppointmentController {

    private final IAppointment appointmentService;
    private final IModelMapperService modelMapperService;

    public AppointmentController(IAppointment appointmentService, IModelMapperService modelMapperService) {
        this.appointmentService = appointmentService;
        this.modelMapperService = modelMapperService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AppointmentResponse> save(@Valid @RequestBody AppointmentSaveRequest appointmentSaveRequest) {
        Appointment saveAppointment = this.modelMapperService.forRequest().map(appointmentSaveRequest, Appointment.class);
        Appointment savedAppointment = appointmentService.save(saveAppointment);
        AppointmentResponse appointmentResponse = modelMapperService.forResponse().map(savedAppointment, AppointmentResponse.class);
        return ResultHelper.created(appointmentResponse);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> get(@PathVariable("id") Long id) {
        Appointment appointment = this.appointmentService.get(id);
        AppointmentResponse appointmentResponse = this.modelMapperService.forResponse().map(appointment, AppointmentResponse.class);
        return ResultHelper.success(appointmentResponse);
    }

    @GetMapping("/animalId")
    @ResponseStatus(HttpStatus.OK)
    public List<AppointmentResponse> findByAppointmentDateBetweenAndAnimalId(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate,
            @RequestParam("animalId") Long animalId
    ) {
        return appointmentService.findByAppointmentDateBetweenAndAnimalId(startDate, endDate, animalId);
    }

    @GetMapping("/doctorId")
    @ResponseStatus(HttpStatus.OK)
    public List<AppointmentResponse> findByAppointmentDateBetweenAndDoctorId(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate,
            @RequestParam("doctorId") Long doctorId
    ) {
        return appointmentService.findByAppointmentDateBetweenAndDoctorId(startDate, endDate, doctorId);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AppointmentResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {

        Page<Appointment> appointmentPage = this.appointmentService.cursor(page, pageSize);
        Page<AppointmentResponse> appointmentResponsePage = appointmentPage
                .map(appointment -> this.modelMapperService.forResponse().map(appointment, AppointmentResponse.class));

        return ResultHelper.cursor(appointmentResponsePage);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> update(@Valid @RequestBody AppointmentUpdateRequest appointmentUpdateRequest) {
        Appointment updateAppointment = this.modelMapperService.forRequest().map(appointmentUpdateRequest, Appointment.class);
        this.appointmentService.update(updateAppointment);
        return ResultHelper.success(this.modelMapperService.forResponse().map(updateAppointment, AppointmentResponse.class));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        this.appointmentService.delete(id);
        return ResultHelper.ok();
    }

}
