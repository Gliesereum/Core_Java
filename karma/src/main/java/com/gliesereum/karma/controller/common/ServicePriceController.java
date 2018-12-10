package com.gliesereum.karma.controller.common;

import com.gliesereum.karma.service.common.ServicePriceService;
import com.gliesereum.share.common.model.dto.karma.common.ServicePriceDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@RestController
@RequestMapping("/price")
public class ServicePriceController {

    @Autowired
    private ServicePriceService service;

    @GetMapping
    public List<ServicePriceDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/package/{id}")
    public List<ServicePriceDto> getAllByPackage(@PathVariable("id") UUID id) {
        return service.getAllByPackage(id);
    }

    @GetMapping("/{id}")
    public ServicePriceDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public ServicePriceDto create(@RequestBody ServicePriceDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public ServicePriceDto update(@RequestBody ServicePriceDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }
}
