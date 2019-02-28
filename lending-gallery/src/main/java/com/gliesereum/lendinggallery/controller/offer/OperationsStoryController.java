package com.gliesereum.lendinggallery.controller.offer;

import com.gliesereum.lendinggallery.service.offer.OperationsStoryService;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OperationsStoryDto;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.util.SecurityUtil;
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
@RequestMapping("/operations-story")
public class OperationsStoryController {

    @Autowired
    private OperationsStoryService service;

    @GetMapping
    public List<OperationsStoryDto> getAll() {
        return service.getAll();
    }

    @GetMapping("by-user")
    public List<OperationsStoryDto> getAllByUser() {
        return service.getAllByUserId(SecurityUtil.getUserId());
    }

    @GetMapping("by-customer/{customerId}")
    public List<OperationsStoryDto> getAllByCustomer(@PathVariable("customerId") UUID customerId) {
        return service.getAllByCustomerId(customerId);
    }

    @GetMapping("/{id}")
    public OperationsStoryDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public OperationsStoryDto create(@Valid @RequestBody OperationsStoryDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public OperationsStoryDto update(@Valid @RequestBody OperationsStoryDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }
}    