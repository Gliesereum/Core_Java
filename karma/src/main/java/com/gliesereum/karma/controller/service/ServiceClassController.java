package com.gliesereum.karma.controller.service;

import com.gliesereum.karma.service.service.ServiceClassService;
import com.gliesereum.share.common.model.dto.karma.service.ServiceClassDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
@RestController
@RequestMapping("/class")
public class ServiceClassController {

    @Autowired
    private ServiceClassService serviceClassService;


    @GetMapping("/{id}")
    public ServiceClassDto getServiceById(@PathVariable("id") UUID id) {
        return serviceClassService.getById(id);
    }

    @GetMapping
    public List<ServiceClassDto> getAllServices() {
        return serviceClassService.getAll();
    }

    @PostMapping
    public ServiceClassDto createService(@Valid @RequestBody ServiceClassDto service) {
        return serviceClassService.create(service);
    }

    @PutMapping
    public ServiceClassDto updateService(@Valid @RequestBody ServiceClassDto service) {
        return serviceClassService.update(service);
    }

    @DeleteMapping("/{id}")
    public MapResponse deleteService(@PathVariable("id") UUID id) {
        serviceClassService.delete(id);
        return new MapResponse("true");
    }
}
