package org.ferhat.vetmanagement.controller;

import jakarta.validation.Valid;
import org.ferhat.vetmanagement.business.abstracts.IAppointment;
import org.ferhat.vetmanagement.core.result.Result;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.appointment.AppointmentResultHelper;
import org.ferhat.vetmanagement.dto.request.appointment.AppointmentSaveRequest;
import org.ferhat.vetmanagement.dto.request.appointment.AppointmentUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.appointment.AppointmentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/appointments")
public class AppointmentController {

    private final IAppointment appointmentService;

    public AppointmentController(IAppointment appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AppointmentResponse> save(@Valid @RequestBody AppointmentSaveRequest appointmentSaveRequest) {
        return appointmentService.save(appointmentSaveRequest);

    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> get(@PathVariable("id") Long id) {
        AppointmentResponse appointmentResponse = this.appointmentService.get(id);
        return AppointmentResultHelper.success(appointmentResponse);
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

        return this.appointmentService.cursor(page, pageSize);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public AppointmentResponse update(@Valid @RequestBody AppointmentUpdateRequest appointmentUpdateRequest) {
        return this.appointmentService.updateAndReturnResponse(appointmentUpdateRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        this.appointmentService.delete(id);
        return AppointmentResultHelper.ok();
    }

}
