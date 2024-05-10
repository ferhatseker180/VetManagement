package org.ferhat.vetmanagement.business.impl;

import org.ferhat.vetmanagement.business.abstracts.IVaccineService;
import org.ferhat.vetmanagement.core.exceptions.NotFoundException;
import org.ferhat.vetmanagement.core.utils.Msg;
import org.ferhat.vetmanagement.entities.Animal;
import org.ferhat.vetmanagement.entities.Vaccine;
import org.ferhat.vetmanagement.repository.VaccineRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VaccineManager implements IVaccineService {
    private final VaccineRepo vaccineRepo;

    public VaccineManager(VaccineRepo vaccineRepo) {
        this.vaccineRepo = vaccineRepo;
    }

    @Override
    public Vaccine save(Vaccine vaccine) {
        return this.vaccineRepo.save(vaccine);
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
}
