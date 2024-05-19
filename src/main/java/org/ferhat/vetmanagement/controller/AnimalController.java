package org.ferhat.vetmanagement.controller;

import jakarta.validation.Valid;
import org.ferhat.vetmanagement.business.abstracts.IAnimalService;
import org.ferhat.vetmanagement.core.config.modelMapper.IModelMapperService;
import org.ferhat.vetmanagement.core.result.Result;
import org.ferhat.vetmanagement.core.result.ResultData;
import org.ferhat.vetmanagement.core.utils.ResultHelper;
import org.ferhat.vetmanagement.dto.request.animal.AnimalSaveRequest;
import org.ferhat.vetmanagement.dto.request.animal.AnimalUpdateRequest;
import org.ferhat.vetmanagement.dto.response.CursorResponse;
import org.ferhat.vetmanagement.dto.response.animal.AnimalResponse;
import org.ferhat.vetmanagement.entities.Animal;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/animals")
public class AnimalController {
    private final IAnimalService animalService;
    private final IModelMapperService modelMapperService;

    public AnimalController(IAnimalService animalService, IModelMapperService modelMapperService) {
        this.animalService = animalService;
        this.modelMapperService = modelMapperService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AnimalResponse> save(@Valid @RequestBody AnimalSaveRequest animalSaveRequest) {
        AnimalResponse animalResponse = animalService.save(animalSaveRequest);
        return ResultHelper.created(animalResponse);
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

        Page<Animal> animalPage = this.animalService.cursor(page, pageSize);
        Page<AnimalResponse> animalResponsePage = animalPage
                .map(animal -> this.modelMapperService.forResponse().map(animal, AnimalResponse.class));

        return ResultHelper.cursor(animalResponsePage);
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
