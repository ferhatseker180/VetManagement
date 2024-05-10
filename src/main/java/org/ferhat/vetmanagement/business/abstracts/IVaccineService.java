package org.ferhat.vetmanagement.business.abstracts;

import org.ferhat.vetmanagement.entities.Animal;
import org.ferhat.vetmanagement.entities.Vaccine;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface IVaccineService {

    Vaccine save(Vaccine vaccine);

    Vaccine update(Vaccine vaccine);

    boolean delete(Long id);

    Vaccine get(Long id);

    List<Vaccine> getAll();

    Page<Vaccine> cursor(int page, int pageSize);

    List<Vaccine> getByAnimalId(Long animalId);

    boolean isExistingVaccine(Animal animal, String name, String code, LocalDate protectionFinishDate);

    List<Vaccine> findByProtectionStartDateBetween(LocalDate startDate, LocalDate endDate);

}
