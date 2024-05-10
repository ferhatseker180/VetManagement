package org.ferhat.vetmanagement.controller;

import jakarta.validation.Valid;
import org.ferhat.vetmanagement.business.abstracts.IAnimalService;
import org.ferhat.vetmanagement.business.abstracts.IVaccineService;
import org.ferhat.vetmanagement.core.config.modelMapper.IModelMapperService;
import org.ferhat.vetmanagement.core.result.Result;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.ResultHelper;
import org.ferhat.vetmanagement.dto.request.vaccine.VaccineSaveRequest;
import org.ferhat.vetmanagement.dto.request.vaccine.VaccineUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.vaccine.VaccineResponse;
import org.ferhat.vetmanagement.entities.Animal;
import org.ferhat.vetmanagement.entities.Vaccine;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/vaccines")
public class VaccineController {
    private final IVaccineService vaccineService;
    private final IModelMapperService modelMapperService;
    private final IAnimalService animalService;

    public VaccineController(IVaccineService vaccineService, IModelMapperService modelMapperService, IAnimalService animalService) {
        this.vaccineService = vaccineService;
        this.modelMapperService = modelMapperService;
        this.animalService = animalService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<VaccineResponse> save(@Valid @RequestBody VaccineSaveRequest vaccineSaveRequest) {
        Long animalId = vaccineSaveRequest.getAnimalId();
        Animal animal = animalService.get(animalId);
        if (animal != null && !vaccineService.isExistingVaccine(animal, vaccineSaveRequest.getName(), vaccineSaveRequest.getCode(), vaccineSaveRequest.getProtectionFinishDate())) {
            Vaccine saveVaccine = modelMapperService.forRequest().map(vaccineSaveRequest, Vaccine.class);
            saveVaccine.setAnimal(animal);
            Vaccine savedVaccine = vaccineService.save(saveVaccine);
            VaccineResponse vaccineResponse = modelMapperService.forResponse().map(savedVaccine, VaccineResponse.class);
            return ResultHelper.created(vaccineResponse);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Vaccine already exists");
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<VaccineResponse> get(@PathVariable("id") Long id) {
        Vaccine vaccine = this.vaccineService.get(id);
        VaccineResponse vaccineResponse = this.modelMapperService.forResponse().map(vaccine, VaccineResponse.class);
        return ResultHelper.success(vaccineResponse);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getAllVaccines() {
        List<Vaccine> vaccines = vaccineService.getAll();
        List<VaccineResponse> vaccineResponses = vaccines.stream()
                .map(vaccine -> modelMapperService.forResponse().map(vaccine, VaccineResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(vaccineResponses);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<VaccineResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {

        Page<Vaccine> vaccinePage = this.vaccineService.cursor(page, pageSize);
        Page<VaccineResponse> vaccineResponsePage = vaccinePage
                .map(vaccine -> this.modelMapperService.forResponse().map(vaccine, VaccineResponse.class));

        return ResultHelper.cursor(vaccineResponsePage);
    }

    @GetMapping("/animal/{animalId}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getVaccinesByAnimalId(@PathVariable("animalId") Long animalId) {
        List<Vaccine> vaccines = vaccineService.getByAnimalId(animalId);
        if (vaccines.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal don't have vaccine");
        }
        List<VaccineResponse> vaccineResponses = vaccines.stream()
                .map(vaccine -> modelMapperService.forResponse().map(vaccine, VaccineResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(vaccineResponses);
    }

    @GetMapping("/date")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getVaccinesByProtectionStartDateBetween(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<Vaccine> vaccines = vaccineService.findByProtectionStartDateBetween(startDate, endDate);
        List<VaccineResponse> vaccineResponses = vaccines.stream()
                .map(vaccine -> modelMapperService.forResponse().map(vaccine, VaccineResponse.class))
                .collect(Collectors.toList());
        return ResultHelper.success(vaccineResponses);
    }


    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<VaccineResponse> update(@Valid @RequestBody VaccineUpdateRequest vaccineUpdateRequest) {
        Vaccine updateVaccine = this.modelMapperService.forRequest().map(vaccineUpdateRequest, Vaccine.class);
        this.vaccineService.update(updateVaccine);
        return ResultHelper.success(this.modelMapperService.forResponse().map(updateVaccine, VaccineResponse.class));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        this.vaccineService.delete(id);
        return ResultHelper.ok();
    }

}
