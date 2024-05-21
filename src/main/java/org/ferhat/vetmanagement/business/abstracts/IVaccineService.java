package org.ferhat.vetmanagement.business.abstracts;

import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.dto.request.vaccine.VaccineSaveRequest;
import org.ferhat.vetmanagement.dto.request.vaccine.VaccineUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.vaccine.VaccineResponse;
import org.ferhat.vetmanagement.entities.Animal;
import org.ferhat.vetmanagement.entities.Vaccine;

import java.time.LocalDate;
import java.util.List;

public interface IVaccineService {

    ResultData<VaccineResponse> save(VaccineSaveRequest vaccineSaveRequest);

    ResultData<VaccineResponse> update(VaccineUpdateRequest vaccineUpdateRequest);

    ResultData<Boolean> delete(Long id);

    Vaccine get(Long id);

    ResultData<List<VaccineResponse>> getAll();

    ResultData<CursorResponse<VaccineResponse>> cursor(int page, int pageSize);

    List<Vaccine> getByAnimalId(Long animalId);

    boolean isExistingVaccine(Animal animal, String name, String code, LocalDate protectionFinishDate);

    List<Vaccine> findByProtectionStartDateBetween(LocalDate startDate, LocalDate endDate);

    List<VaccineResponse> mapToResponse(List<Vaccine> vaccines);

    ResultData<VaccineResponse> getVaccineResponseById(Long id);

    ResultData<List<VaccineResponse>> getVaccineResponsesByAnimalId(Long animalId);

    ResultData<List<VaccineResponse>> getVaccineResponsesByDateRange(LocalDate startDate, LocalDate endDate);

}
