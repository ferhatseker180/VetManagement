package org.ferhat.vetmanagement.business.impl;

import org.ferhat.vetmanagement.business.abstracts.IDoctorService;
import org.ferhat.vetmanagement.core.config.modelMapper.IModelMapperService;
import org.ferhat.vetmanagement.core.exceptions.NotFoundException;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.doctor.DoctorMessage;
import org.ferhat.vetmanagement.core.utils.doctor.DoctorResultHelper;
import org.ferhat.vetmanagement.dto.request.doctor.DoctorSaveRequest;
import org.ferhat.vetmanagement.dto.request.doctor.DoctorUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.doctor.DoctorResponse;
import org.ferhat.vetmanagement.entities.Doctor;
import org.ferhat.vetmanagement.repository.DoctorRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorManager implements IDoctorService {
    private final DoctorRepo doctorRepo;
    private final IModelMapperService modelMapperService;

    public DoctorManager(DoctorRepo doctorRepo, IModelMapperService modelMapperService) {
        this.doctorRepo = doctorRepo;
        this.modelMapperService = modelMapperService;
    }

    @Override
    public DoctorResponse save(DoctorSaveRequest doctorSaveRequest) {
        Doctor saveDoctor = modelMapperService.forRequest().map(doctorSaveRequest, Doctor.class);
        Doctor savedDoctor = this.doctorRepo.save(saveDoctor);
        return modelMapperService.forResponse().map(savedDoctor, DoctorResponse.class);
    }

    @Override
    public DoctorResponse update(DoctorUpdateRequest doctorUpdateRequest) {
        Doctor updateDoctor = modelMapperService.forRequest().map(doctorUpdateRequest, Doctor.class);
        this.get(updateDoctor.getId());
        Doctor updatedDoctor = doctorRepo.save(updateDoctor);
        return modelMapperService.forResponse().map(updatedDoctor, DoctorResponse.class);
    }

    @Override
    public String delete(Long id) {
        Doctor doctor = this.doctorRepo.findById(id).orElseThrow(() -> new NotFoundException(DoctorMessage.NOT_FOUND));
        this.doctorRepo.delete(doctor);
        return DoctorMessage.DELETED;
    }

    @Override
    public DoctorResponse get(Long id) {
        Doctor doctor = this.doctorRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(DoctorMessage.NOT_FOUND));
        return modelMapperService.forResponse().map(doctor, DoctorResponse.class);
    }

    // Show All Doctor
    @Override
    public List<DoctorResponse> getAll() {
        List<Doctor> doctors = doctorRepo.findAll();
        return doctors.stream()
                .map(doctor -> modelMapperService.forResponse().map(doctor, DoctorResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public ResultData<CursorResponse<DoctorResponse>> cursor(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Doctor> doctorPage = this.doctorRepo.findAll(pageable);
        Page<DoctorResponse> doctorResponsePage = doctorPage
                .map(doctor -> this.modelMapperService.forResponse().map(doctor, DoctorResponse.class));
        return DoctorResultHelper.cursor(doctorResponsePage);
    }
}
