package org.ferhat.vetmanagement.controller;

import jakarta.validation.Valid;
import org.ferhat.vetmanagement.business.abstracts.IAvailableDateService;
import org.ferhat.vetmanagement.core.result.Result;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.availableDate.AvailableDateResultHelper;
import org.ferhat.vetmanagement.dto.request.availableDate.AvailableSaveRequest;
import org.ferhat.vetmanagement.dto.request.availableDate.AvailableUpdateRequest;
import org.ferhat.vetmanagement.dto.response.availableDate.AvailableDateResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/availableDates")
public class AvailableDateController {
    private final IAvailableDateService availableDateService;

    public AvailableDateController(IAvailableDateService availableDateService) {
        this.availableDateService = availableDateService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AvailableDateResponse> save(@Valid @RequestBody AvailableSaveRequest availableSaveRequest) {
        AvailableDateResponse response = availableDateService.save(availableSaveRequest);
        return AvailableDateResultHelper.created(response);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AvailableDateResponse> get(@PathVariable("id") Long id) {
        AvailableDateResponse availableDateResponse = this.availableDateService.get(id);
        return AvailableDateResultHelper.success(availableDateResponse);
    }


    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AvailableDateResponse> update(@Valid @RequestBody AvailableUpdateRequest availableUpdateRequest) {
        AvailableDateResponse updatedAvailableResponse = this.availableDateService.update(availableUpdateRequest);
        return AvailableDateResultHelper.success(updatedAvailableResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        this.availableDateService.delete(id);
        return AvailableDateResultHelper.ok();
    }


}
