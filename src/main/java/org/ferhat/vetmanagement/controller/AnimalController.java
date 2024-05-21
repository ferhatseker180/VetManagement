package org.ferhat.vetmanagement.controller;

import jakarta.validation.Valid;
import org.ferhat.vetmanagement.business.abstracts.IAnimalService;
import org.ferhat.vetmanagement.core.result.Result;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.ResultHelper;
import org.ferhat.vetmanagement.dto.request.animal.AnimalSaveRequest;
import org.ferhat.vetmanagement.dto.request.animal.AnimalUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.animal.AnimalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/animals")
public class AnimalController {
    private final IAnimalService animalService;

    public AnimalController(IAnimalService animalService) {
        this.animalService = animalService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AnimalResponse> save(@Valid @RequestBody AnimalSaveRequest animalSaveRequest) {
        return animalService.save(animalSaveRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> get(@PathVariable("id") Long id) {
        AnimalResponse animalResponse = animalService.getAnimalResponseById(id);
        return ResultHelper.success(animalResponse);
    }

    // Customer isme göre arama
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalResponse>> findAnimalsByNameIgnoreCase(@RequestParam("name") String name) {
        List<AnimalResponse> animalResponses = animalService.findAnimalsByNameIgnoreCase(name);
        return ResultHelper.success(animalResponses);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AnimalResponse>> cursor(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {

        return this.animalService.cursor(page, pageSize);

    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public AnimalResponse update(@Valid @RequestBody AnimalUpdateRequest animalUpdateRequest) {
        return this.animalService.updateAndReturnResponse(animalUpdateRequest);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        this.animalService.delete(id);
        return ResultHelper.ok();
    }

}
