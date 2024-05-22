package org.ferhat.vetmanagement.controller;

import jakarta.validation.Valid;
import org.ferhat.vetmanagement.business.abstracts.IVaccineService;
import org.ferhat.vetmanagement.core.result.Result;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.vaccine.VaccineResultHelper;
import org.ferhat.vetmanagement.dto.request.vaccine.VaccineSaveRequest;
import org.ferhat.vetmanagement.dto.request.vaccine.VaccineUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.vaccine.VaccineResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/vaccines")
public class VaccineController {
    private final IVaccineService vaccineService;

    public VaccineController(IVaccineService vaccineService) {
        this.vaccineService = vaccineService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<VaccineResponse> save(@Valid @RequestBody VaccineSaveRequest vaccineSaveRequest) {
        return vaccineService.save(vaccineSaveRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<VaccineResponse> get(@PathVariable("id") Long id) {
        return vaccineService.getVaccineResponseById(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getAllVaccines() {
        return vaccineService.getAll();
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<VaccineResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {

        return vaccineService.cursor(page, pageSize);
    }

    @GetMapping("/animal/{animalId}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getVaccinesByAnimalId(@PathVariable("animalId") Long animalId) {
        return vaccineService.getVaccineResponsesByAnimalId(animalId);
    }

    @GetMapping("/date")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getVaccinesByProtectionStartDateBetween(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return vaccineService.getVaccineResponsesByDateRange(startDate, endDate);
    }


    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<VaccineResponse> update(@Valid @RequestBody VaccineUpdateRequest vaccineUpdateRequest) {
        return vaccineService.update(vaccineUpdateRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        vaccineService.delete(id);
        return VaccineResultHelper.ok();
    }

}
