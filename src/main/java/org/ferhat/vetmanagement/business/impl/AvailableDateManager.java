package org.ferhat.vetmanagement.business.impl;

import org.ferhat.vetmanagement.business.abstracts.IAvailableDateService;
import org.ferhat.vetmanagement.core.config.modelMapper.IModelMapperService;
import org.ferhat.vetmanagement.core.exceptions.NotFoundException;
import org.ferhat.vetmanagement.core.utils.availableDate.AvailableDateMessage;
import org.ferhat.vetmanagement.dto.request.availableDate.AvailableSaveRequest;
import org.ferhat.vetmanagement.dto.request.availableDate.AvailableUpdateRequest;
import org.ferhat.vetmanagement.dto.response.availableDate.AvailableDateResponse;
import org.ferhat.vetmanagement.dto.response.doctor.DoctorResponse;
import org.ferhat.vetmanagement.entities.AvailableDate;
import org.ferhat.vetmanagement.entities.Doctor;
import org.ferhat.vetmanagement.repository.AvailableDateRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvailableDateManager implements IAvailableDateService {
    private final AvailableDateRepo availableDateRepo;
    private final IModelMapperService modelMapperService;

    public AvailableDateManager(AvailableDateRepo availableDateRepo, IModelMapperService modelMapperService) {
        this.availableDateRepo = availableDateRepo;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public AvailableDateResponse save(AvailableSaveRequest availableSaveRequest) {
        Doctor doctor = availableSaveRequest.getDoctor();
        LocalDate date = availableSaveRequest.getAvailableDate();

        // Is there a doctor with the specified ID number?
        if (doctor == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, AvailableDateMessage.NOT_FOUND);
        }

        // List of available dates for the doctor with the specified ID number
        List<AvailableDate> existingAvailableDates = findByDoctorIdAndAvailableDate(doctor.getId(), date);

        // If the doctor is available on the specified date
        if (!existingAvailableDates.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, AvailableDateMessage.EXISTING_APPOINTMENT);
        }
        AvailableDate availableDate = new AvailableDate();
        availableDate.setDoctor(doctor);
        availableDate.setAvailableDate(date);

        AvailableDate savedAvailableDate = availableDateRepo.save(availableDate);
        return mapToResponse(savedAvailableDate);
    }

    @Override
    public AvailableDateResponse update(AvailableUpdateRequest availableUpdateRequest) {
        AvailableDate updateAvailable = this.modelMapperService.forRequest().map(availableUpdateRequest, AvailableDate.class);
        this.get(updateAvailable.getId()); // Check available has or not
        AvailableDate updatedAvailableDate = this.availableDateRepo.save(updateAvailable);
        return modelMapperService.forResponse().map(updatedAvailableDate, AvailableDateResponse.class);
    }

    @Override
    public String delete(Long id) {
        AvailableDate availableDate = this.availableDateRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(AvailableDateMessage.NOT_FOUND));
        this.availableDateRepo.delete(availableDate);
        return AvailableDateMessage.DELETED;
    }

    @Override
    public AvailableDateResponse get(Long id) {
        AvailableDate availableDate = this.availableDateRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(AvailableDateMessage.NOT_FOUND));
        return modelMapperService.forResponse().map(availableDate, AvailableDateResponse.class);
    }


    // Convert to Response
    @Override
    public AvailableDateResponse mapToResponse(AvailableDate availableDate) {
        Doctor doctor = availableDate.getDoctor();
        DoctorResponse doctorResponse = modelMapperService.forResponse().map(doctor, DoctorResponse.class);

        AvailableDateResponse response = modelMapperService.forResponse().map(availableDate, AvailableDateResponse.class);
        response.setDoctor(doctorResponse);
        return response;
    }

    // List available times by doctor's ID and selected date
    @Override
    public List<AvailableDate> findByDoctorIdAndAvailableDate(Long doctorId, LocalDate date) {
        return availableDateRepo.findByDoctorIdAndAvailableDate(doctorId, date);

    }
}
