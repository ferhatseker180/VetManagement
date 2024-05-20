package org.ferhat.vetmanagement.controller;

import jakarta.validation.Valid;
import org.ferhat.vetmanagement.business.abstracts.IAvailableDateService;
import org.ferhat.vetmanagement.core.config.modelMapper.IModelMapperService;
import org.ferhat.vetmanagement.core.result.Result;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.ResultHelper;
import org.ferhat.vetmanagement.dto.request.availableDate.AvailableSaveRequest;
import org.ferhat.vetmanagement.dto.request.availableDate.AvailableUpdateRequest;
import org.ferhat.vetmanagement.dto.response.availableDate.AvailableDateResponse;
import org.ferhat.vetmanagement.entities.AvailableDate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/availableDates")
public class AvailableDateController {
    private final IAvailableDateService availableDateService;
    private final IModelMapperService modelMapperService;

    public AvailableDateController(IAvailableDateService availableDateService, IModelMapperService modelMapperService) {
        this.availableDateService = availableDateService;
        this.modelMapperService = modelMapperService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AvailableDateResponse> save(@Valid @RequestBody AvailableSaveRequest availableSaveRequest) {
        AvailableDateResponse response = availableDateService.save(availableSaveRequest);
        return ResultHelper.created(response);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AvailableDateResponse> get(@PathVariable("id") Long id) {

        AvailableDate availableDate = this.availableDateService.get(id);
        AvailableDateResponse availableDateResponse = this.modelMapperService.forResponse().map(availableDate, AvailableDateResponse.class);
        return ResultHelper.success(availableDateResponse);
    }


    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AvailableDateResponse> update(@Valid @RequestBody AvailableUpdateRequest availableUpdateRequest) {
        AvailableDate updateAvailable = this.modelMapperService.forRequest().map(availableUpdateRequest, AvailableDate.class);
        this.availableDateService.update(updateAvailable);
        return ResultHelper.success(this.modelMapperService.forResponse().map(updateAvailable, AvailableDateResponse.class));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        this.availableDateService.delete(id);
        return ResultHelper.ok();
    }


}
