package com.gliesereum.karma.controller.service;

import com.gliesereum.karma.service.service.ServiceClassPriceService;
import com.gliesereum.karma.service.service.ServicePriceService;
import com.gliesereum.share.common.model.dto.karma.service.ServiceClassPriceDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
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
@RequestMapping("/price")
public class ServicePriceController {

    @Autowired
    private ServicePriceService servicePriceService;

    @Autowired
    private ServiceClassPriceService serviceClassPriceService;

    @GetMapping
    public List<ServicePriceDto> getAll() {
        return servicePriceService.getAll();
    }

    @GetMapping("/package/{id}")
    public List<ServicePriceDto> getAllByPackage(@PathVariable("id") UUID id) {
        return servicePriceService.getAllByPackage(id);
    }

    @GetMapping("/{id}")
    public ServicePriceDto getById(@PathVariable("id") UUID id) {
        return servicePriceService.getById(id);
    }

    @PostMapping
    public ServicePriceDto create(@Valid @RequestBody ServicePriceDto dto) {
        return servicePriceService.create(dto);
    }

    @PutMapping
    public ServicePriceDto update(@Valid @RequestBody ServicePriceDto dto) {
        return servicePriceService.update(dto);
    }

    @GetMapping("/by-business/{id}")
    public List<ServicePriceDto> getByBusinessId(@PathVariable("id") UUID id) {
        return servicePriceService.getByBusinessId(id);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        servicePriceService.delete(id);
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

    @DeleteMapping("/class/{priceId}/{classId}")
    public MapResponse deleteClass(@PathVariable("priceId") UUID priceId, @PathVariable("classId") UUID classId) {
        serviceClassPriceService.delete(priceId, classId);
        return new MapResponse("true");
    }

    @GetMapping("/{priceId}/class")
    public List<ServiceClassPriceDto> getByPriceId(@PathVariable("priceId") UUID priceId) {
        return serviceClassPriceService.getByPriceId(priceId);
    }

    @PostMapping("/filter-attribute/{idPrice}/{idAttribute}")
    public ServicePriceDto addFilterAttribute(@PathVariable("idPrice") UUID idPrice, @PathVariable("idAttribute") UUID idAttribute) {
        return servicePriceService.addFilterAttribute(idPrice, idAttribute);
    }

    @DeleteMapping("/remove/filter-attribute/{idPrice}/{idAttribute}")
    public ServicePriceDto removeFilterAttribute(@PathVariable("idPrice") UUID idPrice, @PathVariable("idAttribute") UUID idAttribute) {
        return servicePriceService.removeFilterAttribute(idPrice, idAttribute);
    }

}
