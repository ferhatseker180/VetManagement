package org.ferhat.vetmanagement.controller;

import jakarta.validation.Valid;
import org.ferhat.vetmanagement.business.abstracts.IDoctorService;
import org.ferhat.vetmanagement.core.result.Result;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.ResultHelper;
import org.ferhat.vetmanagement.dto.request.doctor.DoctorSaveRequest;
import org.ferhat.vetmanagement.dto.request.doctor.DoctorUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.doctor.DoctorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/doctors")
public class DoctorController {
    private final IDoctorService doctorService;

    public DoctorController(IDoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<DoctorResponse> save(@Valid @RequestBody DoctorSaveRequest doctorSaveRequest) {
        DoctorResponse doctorResponse = this.doctorService.save(doctorSaveRequest);
        return ResultHelper.created(doctorResponse);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<DoctorResponse> get(@PathVariable("id") Long id) {
        DoctorResponse doctorResponse = this.doctorService.get(id);
        return ResultHelper.success(doctorResponse);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<DoctorResponse>> getAllDoctors() {
        List<DoctorResponse> doctorResponses = doctorService.getAll();
        return ResultHelper.success(doctorResponses);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<DoctorResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {

        return this.doctorService.cursor(page, pageSize);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<DoctorResponse> update(@Valid @RequestBody DoctorUpdateRequest doctorUpdateRequest) {
        DoctorResponse doctorResponse = this.doctorService.update(doctorUpdateRequest);
        return ResultHelper.success(doctorResponse);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        this.doctorService.delete(id);
        return ResultHelper.ok();
    }

}
