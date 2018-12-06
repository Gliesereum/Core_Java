package com.gliesereum.karma.controller.car;

import com.gliesereum.karma.service.car.*;
import com.gliesereum.share.common.model.dto.karma.car.*;
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
 * @since 12/5/18
 */
@RestController
@RequestMapping("/car")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private BrandCarService brandCarService;

    @Autowired
    private ModelCarService modelCarService;

    @Autowired
    private YearCarService yearCarService;

    @Autowired
    private ServiceClassCarService serviceClassCarService;

    @GetMapping("/{id}")
    public CarDto getById(@PathVariable("id") UUID id) {
        return carService.getById(id);
    }

    @GetMapping("/user")
    public List<CarDto> getAll() {
        return carService.getAllByUserId(SecurityUtil.getUserId());
    }

    @PostMapping
    public CarDto create(@RequestBody CarDto car) {
        return carService.create(car);
    }

    @PostMapping("/add/service/{idCar}/{idService}")
    public CarDto addService(@PathVariable("idCar") UUID idCar, @PathVariable("idService") UUID idService) {
        return carService.addService(idCar, idService);
    }

    @DeleteMapping("/remove/service/{idCar}/{idService}")
    public CarDto removeService(@PathVariable("idCar") UUID idCar, @PathVariable("idService") UUID idService) {
        return carService.removeService(idCar, idService);
    }

    @PutMapping
    public CarDto update(@Valid @RequestBody CarDto car) {
        return carService.update(car);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        carService.delete(id);
        return new MapResponse("true");
    }

    @DeleteMapping("/by/user/{id}")
    public MapResponse deleteByUserId(@PathVariable("id") UUID id) {
        carService.deleteByUserId(id);
        //TODO: notify other service
        return new MapResponse("true");
    }

    @GetMapping("/brands")
    public List<BrandCarDto> getAllBrands() {
        return brandCarService.getAll();
    }

    @GetMapping("/models")
    public List<ModelCarDto> getAllModels() {
        return modelCarService.getAll();
    }

    @GetMapping("/models/by/brand/{id}")
    public List<ModelCarDto> getAllModelsByBrandId(@PathVariable("id") UUID id) {
        return modelCarService.getAllByBrandId(id);
    }

    @GetMapping("/years")
    public List<YearCarDto> getAllYears() {
        return yearCarService.getAll();
    }

    @GetMapping("/service/{id}")
    public ServiceClassCarDto getServiceById(@PathVariable("id") UUID id) {
        return serviceClassCarService.getById(id);
    }

    @GetMapping("/services")
    public List<ServiceClassCarDto> getAllServices() {
        return serviceClassCarService.getAll();
    }

    @PostMapping("/service")
    public ServiceClassCarDto createService(@RequestBody ServiceClassCarDto service) {
        return serviceClassCarService.create(service);
    }

    @PutMapping("/service")
    public ServiceClassCarDto updateService(@Valid @RequestBody ServiceClassCarDto service) {
        return serviceClassCarService.update(service);
    }

    @DeleteMapping("/service/{id}")
    public MapResponse deleteService(@PathVariable("id") UUID id) {
        serviceClassCarService.delete(id);
        return new MapResponse("true");
    }
}
