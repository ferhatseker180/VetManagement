package org.ferhat.vetmanagement.business.abstracts;

import org.ferhat.vetmanagement.dto.request.availableDate.AvailableSaveRequest;
import org.ferhat.vetmanagement.dto.request.availableDate.AvailableUpdateRequest;
import org.ferhat.vetmanagement.dto.response.availableDate.AvailableDateResponse;
import org.ferhat.vetmanagement.entities.AvailableDate;

import java.time.LocalDate;
import java.util.List;

public interface IAvailableDateService {

    AvailableDateResponse save(AvailableSaveRequest availableSaveRequest);

    AvailableDateResponse update(AvailableUpdateRequest availableUpdateRequest);

    String delete(Long id);

    AvailableDateResponse get(Long id);

    AvailableDateResponse mapToResponse(AvailableDate availableDate);

    List<AvailableDate> findByDoctorIdAndAvailableDate(Long doctorId, LocalDate date);
}
