package org.ferhat.vetmanagement.business.abstracts;

import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.dto.request.doctor.DoctorSaveRequest;
import org.ferhat.vetmanagement.dto.request.doctor.DoctorUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.doctor.DoctorResponse;

import java.util.List;

public interface IDoctorService {

    DoctorResponse save(DoctorSaveRequest doctorSaveRequest);

    DoctorResponse update(DoctorUpdateRequest doctorUpdateRequest);

    boolean delete(Long id);

    DoctorResponse get(Long id);

    List<DoctorResponse> getAll();

    ResultData<CursorResponse<DoctorResponse>> cursor(int page, int pageSize);
}
