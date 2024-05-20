package org.ferhat.vetmanagement.business.impl;

import org.ferhat.vetmanagement.business.abstracts.IAnimalService;
import org.ferhat.vetmanagement.business.abstracts.IVaccineService;
import org.ferhat.vetmanagement.core.config.modelMapper.IModelMapperService;
import org.ferhat.vetmanagement.core.exceptions.NotFoundException;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.Msg;
import org.ferhat.vetmanagement.core.utils.ResultHelper;
import org.ferhat.vetmanagement.dto.request.vaccine.VaccineSaveRequest;
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
    private final IAnimalService animalService;
    private final IModelMapperService modelMapperService;

    public VaccineManager(VaccineRepo vaccineRepo, IAnimalService animalService, IModelMapperService modelMapperService) {
        this.vaccineRepo = vaccineRepo;
        this.animalService = animalService;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public ResultData<VaccineResponse> save(VaccineSaveRequest vaccineSaveRequest) {
        // Convert to Vaccine Object
        Vaccine vaccineToSave = modelMapperService.forRequest().map(vaccineSaveRequest, Vaccine.class);

        // Check vet and animal
        Animal animal = vaccineToSave.getAnimal();
        if (animal == null) {
            throw new NotFoundException("Hayvan bulunamadı");
        }

        // Check Available Same Name ve Same Finish Protection Time
        if (isExistingVaccine(animal, vaccineToSave.getName(), vaccineToSave.getCode(), vaccineToSave.getProtectionFinishDate())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Aşı zaten mevcut");
        }

        // Save Vaccine
        Vaccine savedVaccine = vaccineRepo.save(vaccineToSave);

        // Convert to Vaccine
        VaccineResponse vaccineResponse = modelMapperService.forResponse().map(savedVaccine, VaccineResponse.class);

        return ResultHelper.created(vaccineResponse);
    }

    @Override
    public Vaccine update(Vaccine vaccine) {
        this.get(vaccine.getId());
        return this.vaccineRepo.save(vaccine);
    }

    @Override
    public boolean delete(Long id) {
        Vaccine vaccine = this.get(id);
        this.vaccineRepo.delete(vaccine);
        return true;
    }

    @Override
    public Vaccine get(Long id) {
        return this.vaccineRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public List<Vaccine> getAll() {
        return this.vaccineRepo.findAll();
    }

    @Override
    public Page<Vaccine> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.vaccineRepo.findAll(pageable);
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

}
