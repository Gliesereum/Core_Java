package com.gliesereum.karma.controller.car;

import com.gliesereum.karma.service.car.ServiceClassCarService;
import com.gliesereum.share.common.model.dto.karma.car.ServiceClassCarDto;
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
@RequestMapping("/service/class")
public class ServiceClassController {

    @Autowired
    private ServiceClassCarService serviceClassCarService;


    @GetMapping("/{id}")
    public ServiceClassCarDto getServiceById(@PathVariable("id") UUID id) {
        return serviceClassCarService.getById(id);
    }

    @GetMapping
    public List<ServiceClassCarDto> getAllServices() {
        return serviceClassCarService.getAll();
    }

    @PostMapping
    public ServiceClassCarDto createService(@RequestBody ServiceClassCarDto service) {
        return serviceClassCarService.create(service);
    }

    @PutMapping
    public ServiceClassCarDto updateService(@Valid @RequestBody ServiceClassCarDto service) {
        return serviceClassCarService.update(service);
    }

    @DeleteMapping("/{id}")
    public MapResponse deleteService(@PathVariable("id") UUID id) {
        serviceClassCarService.delete(id);
        return new MapResponse("true");
    }
}
