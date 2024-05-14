package org.ferhat.vetmanagement.business.abstracts;

import org.ferhat.vetmanagement.entities.AvailableDate;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface IAvailableDateService {

    AvailableDate save(AvailableDate availableDate);

    AvailableDate update(AvailableDate availableDate);

    boolean delete(Long id);

    AvailableDate get(Long id);

    Page<AvailableDate> cursor(int page, int pageSize);

     List<AvailableDate> findByDoctorIdAndAvailableDate(Long doctorId, LocalDate date);
}
