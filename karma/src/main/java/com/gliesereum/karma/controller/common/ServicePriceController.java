package com.gliesereum.karma.controller.common;

import com.gliesereum.karma.service.common.ServiceClassPriceService;
import com.gliesereum.karma.service.common.ServicePriceService;
import com.gliesereum.share.common.model.dto.karma.common.ServiceClassPriceDto;
import com.gliesereum.share.common.model.dto.karma.common.ServicePriceDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @Autowired
    private ServiceClassPriceService serviceClassPriceService;

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

    @PostMapping("/class")
    public ServiceClassPriceDto createClass(@Valid @RequestBody ServiceClassPriceDto dto) {
        return serviceClassPriceService.create(dto);
    }

    @PutMapping("/class")
    public ServiceClassPriceDto updateClass(@Valid @RequestBody ServiceClassPriceDto dto) {
        return serviceClassPriceService.update(dto);
    }

    @DeleteMapping("/class/{id}")
    public MapResponse deleteClass(@PathVariable("id") UUID id) {
        serviceClassPriceService.delete(id);
        return new MapResponse("true");
    }

    @GetMapping("/{priceId}/class")
    private List<ServiceClassPriceDto> getByPriceId(@PathVariable("id") UUID priceId) {
        return serviceClassPriceService.getByPriceId(priceId);
    }

}
