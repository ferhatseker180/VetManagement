package org.ferhat.vetmanagement.business.impl;

import org.ferhat.vetmanagement.business.abstracts.IAvailableDateService;
import org.ferhat.vetmanagement.core.exceptions.NotFoundException;
import org.ferhat.vetmanagement.core.utils.Msg;
import org.ferhat.vetmanagement.entities.AvailableDate;
import org.ferhat.vetmanagement.repository.AvailableDateRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvailableDateManager implements IAvailableDateService {
    private final AvailableDateRepo availableDateRepo;

    public AvailableDateManager(AvailableDateRepo availableDateRepo) {
        this.availableDateRepo = availableDateRepo;
    }

    @Override
    public AvailableDate save(AvailableDate availableDate) {
        return this.availableDateRepo.save(availableDate);
    }

    @Override
    public AvailableDate update(AvailableDate availableDate) {
        this.get(availableDate.getId());
        return this.availableDateRepo.save(availableDate);
    }

    @Override
    public boolean delete(Long id) {
        AvailableDate availableDate = this.get(id);
        this.availableDateRepo.delete(availableDate);
        return true;
    }

    @Override
    public AvailableDate get(Long id) {
        return this.availableDateRepo.findById(id).orElseThrow(() -> new NotFoundException(Msg.NOT_FOUND));
    }

    @Override
    public Page<AvailableDate> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.availableDateRepo.findAll(pageable);
    }

    @Override
    public List<AvailableDate> findByDoctorIdAndAvailableDate(Long doctorId, LocalDate date) {
        return availableDateRepo.findByDoctorIdAndAvailableDate(doctorId, date);

    }
}
