package com.gliesereum.karma.controller.business;

import com.gliesereum.karma.service.business.BusinessCategoryService;
import com.gliesereum.share.common.model.dto.karma.business.BusinessCategoryDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.BusinessType;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@RestController
public class BusinessCategoryController {

    @Autowired
    private BusinessCategoryService businessCategoryService;

    @GetMapping("/{id}")
    public BusinessCategoryDto getById(@PathVariable("id") UUID id) {
        return businessCategoryService.getById(id);
    }

    @GetMapping
    public List<BusinessCategoryDto> getAll() {
        return businessCategoryService.getAll();
    }

    @GetMapping("/by-business-type")
    public List<BusinessCategoryDto> getByBusinessType(@RequestParam("businessType") BusinessType businessType) {
        return businessCategoryService.getByBusinessType(businessType);
    }

    @GetMapping("/business-type")
    public MapResponse getBusinessTypes() {
        return new MapResponse("types", EnumSet.allOf(BusinessType.class));
    }

    @GetMapping("/by-code")
    public BusinessCategoryDto getByCode(@RequestParam("code") String code) {
        return businessCategoryService.getByCode(code);
    }

    @GetMapping("/exists/by-code")
    public MapResponse existsByCode(@RequestParam("code") String code) {
        boolean result = businessCategoryService.existByCode(code);
        return new MapResponse(result);
    }

    @PostMapping
    public BusinessCategoryDto create(@RequestBody @Valid BusinessCategoryDto workTime) {
        return businessCategoryService.create(workTime);
    }


    @PutMapping
    public BusinessCategoryDto update(@RequestBody @Valid BusinessCategoryDto workTime) {
        return businessCategoryService.update(workTime);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        businessCategoryService.delete(id);
        return new MapResponse("true");
    }
}
