package com.gliesereum.karma.controller.filter;

import com.gliesereum.karma.service.filter.FilterAttributeService;
import com.gliesereum.karma.service.filter.FilterService;
import com.gliesereum.share.common.model.dto.karma.filter.FilterAttributeDto;
import com.gliesereum.share.common.model.dto.karma.filter.FilterDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@RestController
@RequestMapping("/filter")
public class FilterController {

    @Autowired
    private FilterService filterService;

    @Autowired
    private FilterAttributeService attributeService;

    @GetMapping
    public List<FilterDto> getAllFilter() {
        return filterService.getAll();
    }

    @GetMapping("/{id}")
    public FilterDto getFilterById(@PathVariable("id") UUID id) {
        return filterService.getById(id);
    }

    @PostMapping
    public FilterDto createFilter(@Valid @RequestBody FilterDto dto) {
        return filterService.create(dto);
    }

    @PutMapping
    public FilterDto updateFilter(@Valid @RequestBody FilterDto dto) {
        return filterService.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse deleteFiler(@PathVariable("id") UUID id) {
        filterService.delete(id);
        return new MapResponse("true");
    }

    @GetMapping("/by-business-category")
    public List<FilterDto> getByServiceType(@RequestParam("businessCategoryId") UUID businessCategoryId) {
        return filterService.getAllByBusinessCategoryId(businessCategoryId);
    }

    @GetMapping("/attribute")
    public List<FilterAttributeDto> getAllAttribute() {
        return attributeService.getAll();
    }

    @GetMapping("/attribute/{id}")
    public FilterAttributeDto getAttributeById(@PathVariable("id") UUID id) {
        return attributeService.getById(id);
    }

    @PostMapping("/attribute")
    public FilterAttributeDto createAttribute(@Valid @RequestBody FilterAttributeDto dto) {
        return attributeService.create(dto);
    }

    @PutMapping("/attribute")
    public FilterAttributeDto updateAttribute(@Valid @RequestBody FilterAttributeDto dto) {
        return attributeService.update(dto);
    }

    @DeleteMapping("/attribute/{id}")
    public MapResponse deleteAttribute(@PathVariable("id") UUID id) {
        attributeService.delete(id);
        return new MapResponse("true");
    }

    @GetMapping("/attribute/by-filter-id")
    public List<FilterAttributeDto> getByFilterId(@RequestParam("filterId") UUID filterId) {
        return attributeService.getByFilterId(filterId);
    }
}
