package org.ferhat.vetmanagement.business.impl;

import org.ferhat.vetmanagement.business.abstracts.IVaccineService;
import org.ferhat.vetmanagement.core.config.modelMapper.IModelMapperService;
import org.ferhat.vetmanagement.core.exceptions.NotFoundException;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.animal.AnimalMessage;
import org.ferhat.vetmanagement.core.utils.vaccine.VaccineMessage;
import org.ferhat.vetmanagement.core.utils.vaccine.VaccineResultHelper;
import org.ferhat.vetmanagement.dto.request.vaccine.VaccineSaveRequest;
import org.ferhat.vetmanagement.dto.request.vaccine.VaccineUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.vaccine.VaccineResponse;
import org.ferhat.vetmanagement.entities.Animal;
import org.ferhat.vetmanagement.entities.Vaccine;
import org.ferhat.vetmanagement.repository.VaccineRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VaccineManager implements IVaccineService {
    private final VaccineRepo vaccineRepo;
    private final IModelMapperService modelMapperService;

    public VaccineManager(VaccineRepo vaccineRepo, IModelMapperService modelMapperService) {
        this.vaccineRepo = vaccineRepo;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public ResultData<VaccineResponse> save(VaccineSaveRequest vaccineSaveRequest) {
        // Convert to Vaccine Object
        Vaccine vaccineToSave = modelMapperService.forRequest().map(vaccineSaveRequest, Vaccine.class);

        // Check vet and animal
        Animal animal = vaccineToSave.getAnimal();
        if (animal == null) {
            throw new NotFoundException(AnimalMessage.NOT_FOUND);
        }

        // Check Available Same Name ve Same Finish Protection Time
        if (isExistingVaccine(animal, vaccineToSave.getName(), vaccineToSave.getCode(), vaccineToSave.getProtectionFinishDate())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, VaccineMessage.EXISTING_VACCINE);
        }

        // Save Vaccine
        Vaccine savedVaccine = vaccineRepo.save(vaccineToSave);

        // Convert to Vaccine
        VaccineResponse vaccineResponse = modelMapperService.forResponse().map(savedVaccine, VaccineResponse.class);

        return VaccineResultHelper.created(vaccineResponse);
    }

    @Override
    public ResultData<VaccineResponse> update(VaccineUpdateRequest vaccineUpdateRequest) {
        Vaccine updateVaccine = modelMapperService.forRequest().map(vaccineUpdateRequest, Vaccine.class);
        this.get(updateVaccine.getId());
        Vaccine updatedVaccine = this.vaccineRepo.save(updateVaccine);
        VaccineResponse vaccineResponse = modelMapperService.forResponse().map(updatedVaccine, VaccineResponse.class);
        return VaccineResultHelper.success(vaccineResponse);
    }

    @Override
    public ResultData<String> delete(Long id) {
        Vaccine vaccine = this.get(id);
        if (vaccine == null) {
            throw new NotFoundException(VaccineMessage.NOT_FOUND);
        }
        this.vaccineRepo.delete(vaccine);
        return VaccineResultHelper.success(VaccineMessage.DELETED);
    }

    @Override
    public Vaccine get(Long id) {
        return this.vaccineRepo.findById(id).orElseThrow(() -> new NotFoundException(VaccineMessage.NOT_FOUND));
    }

    @Override
    public ResultData<List<VaccineResponse>> getAll() {
        List<Vaccine> vaccines = this.vaccineRepo.findAll();
        List<VaccineResponse> vaccineResponses = mapToResponse(vaccines);
        return VaccineResultHelper.success(vaccineResponses);
    }

    @Override
    public ResultData<CursorResponse<VaccineResponse>> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Vaccine> vaccinePage = this.vaccineRepo.findAll(pageable);
        Page<VaccineResponse> vaccineResponsePage = vaccinePage
                .map(vaccine -> modelMapperService.forResponse().map(vaccine, VaccineResponse.class));

        return VaccineResultHelper.cursor(vaccineResponsePage);
    }

    @Override
    public List<Vaccine> getByAnimalId(Long animalId) {
        return vaccineRepo.getByAnimalId(animalId);
    }

    @Override
    public boolean isExistingVaccine(Animal animal, String name, String code, LocalDate protectionFinishDate) {
        List<Vaccine> vaccines = this.getByAnimalId(animal.getId());
        for (Vaccine vaccine : vaccines) {
            if (vaccine.getName().equals(name) && vaccine.getCode().equals(code) && vaccine.getProtectionFinishDate().isAfter(LocalDate.now())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Vaccine> findByProtectionStartDateBetween(LocalDate startDate, LocalDate endDate) {
        return vaccineRepo.findByProtectionStartDateBetween(startDate, endDate);
    }

    public List<VaccineResponse> mapToResponse(List<Vaccine> vaccines) {
        return vaccines.stream()
                .map(vaccine -> modelMapperService.forResponse().map(vaccine, VaccineResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public ResultData<VaccineResponse> getVaccineResponseById(Long id) {
        Vaccine vaccine = this.get(id);
        VaccineResponse vaccineResponse = modelMapperService.forResponse().map(vaccine, VaccineResponse.class);
        return VaccineResultHelper.success(vaccineResponse);
    }

    @Override
    public ResultData<List<VaccineResponse>> getVaccineResponsesByAnimalId(Long animalId) {
        List<Vaccine> vaccines = this.getByAnimalId(animalId);
        return VaccineResultHelper.success(mapToResponse(vaccines));
    }

    @Override
    public ResultData<List<VaccineResponse>> getVaccineResponsesByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Vaccine> vaccines = this.findByProtectionStartDateBetween(startDate, endDate);
        return VaccineResultHelper.success(mapToResponse(vaccines));
    }

}
