package org.ferhat.vetmanagement.business.abstracts;

import org.ferhat.vetmanagement.dto.response.doctor.DoctorResponse;
import org.ferhat.vetmanagement.entities.Doctor;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IDoctorService {

    Doctor save(Doctor doctor);

    Doctor update(Doctor doctor);

    boolean delete(Long id);

    Doctor get(Long id);

    List<DoctorResponse> getAll();

    Page<Doctor> cursor(int page, int pageSize);
}
