package org.ferhat.vetmanagement.controller;

import jakarta.validation.Valid;
import org.ferhat.vetmanagement.business.abstracts.IAvailableDateService;
import org.ferhat.vetmanagement.business.abstracts.IDoctorService;
import org.ferhat.vetmanagement.core.config.modelMapper.IModelMapperService;
import org.ferhat.vetmanagement.core.result.Result;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.ResultHelper;
import org.ferhat.vetmanagement.dto.request.availableDate.AvailableSaveRequest;
import org.ferhat.vetmanagement.dto.request.availableDate.AvailableUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.availableDate.AvailableDateResponse;
import org.ferhat.vetmanagement.entities.AvailableDate;
import org.ferhat.vetmanagement.entities.Doctor;
import org.ferhat.vetmanagement.repository.AvailableDateRepo;
import org.ferhat.vetmanagement.repository.DoctorRepo;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/availableDates")
public class AvailableDateController {
    private final IAvailableDateService availableDateService;
    private final AvailableDateRepo availableDateRepo;
    private final IModelMapperService modelMapperService;
    private final IDoctorService doctorService;

    public AvailableDateController(IAvailableDateService availableDateService, AvailableDateRepo availableDateRepo, IModelMapperService modelMapperService, IDoctorService doctorService, DoctorRepo doctorRepo) {
        this.availableDateService = availableDateService;
        this.availableDateRepo = availableDateRepo;
        this.modelMapperService = modelMapperService;
        this.doctorService = doctorService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AvailableDateResponse> save(@Valid @RequestBody AvailableSaveRequest availableSaveRequest) {
        Doctor doctor = availableSaveRequest.getDoctor();
        LocalDate availableDate = availableSaveRequest.getAvailableDate();
      //  Doctor doctor = doctorService.get(doctorId);
        if (doctor == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found");
        }

        List<AvailableDate> existingAvailableDates = availableDateService.findByDoctorIdAndAvailableDate(doctor.getId(), availableDate);

        // Eğer aynı doktor için aynı tarihli bir randevu varsa, hata fırlatılacak
        if (existingAvailableDates.stream().anyMatch(availDate -> availDate.getAvailableDate().equals(availableDate))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An available date for the doctor on this date already exists");
        } else {
            AvailableDate saveAvailable = this.modelMapperService.forRequest().map(availableSaveRequest, AvailableDate.class);
             availableDateService.save(saveAvailable);
            return ResultHelper.created(this.modelMapperService.forResponse().map(saveAvailable, AvailableDateResponse.class));
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AvailableDateResponse> get(@PathVariable("id") Long id) {
        AvailableDate availableDate = this.availableDateService.get(id);
        AvailableDateResponse availableDateResponse = this.modelMapperService.forResponse().map(availableDate, AvailableDateResponse.class);
        return ResultHelper.success(availableDateResponse);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AvailableDateResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {

        Page<AvailableDate> availableDatePage = this.availableDateService.cursor(page, pageSize);
        Page<AvailableDateResponse> availableDateResponsePage = availableDatePage
                .map(availableDate -> this.modelMapperService.forResponse().map(availableDate, AvailableDateResponse.class));

        return ResultHelper.cursor(availableDateResponsePage);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AvailableDateResponse> update(@Valid @RequestBody AvailableUpdateRequest availableUpdateRequest) {
        AvailableDate updateAvailable = this.modelMapperService.forRequest().map(availableUpdateRequest, AvailableDate.class);
        this.availableDateService.update(updateAvailable);
        return ResultHelper.success(this.modelMapperService.forResponse().map(updateAvailable, AvailableDateResponse.class));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        this.availableDateService.delete(id);
        return ResultHelper.ok();
    }

}
