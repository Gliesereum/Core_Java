package com.gliesereum.karma.controller.carwash;

import com.gliesereum.karma.service.carwash.CarWashService;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashDto;
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
@RequestMapping("/carwash")
public class CarWashController {

    @Autowired
    private CarWashService service;

    @GetMapping
    public List<CarWashDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CarWashDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public CarWashDto create(@RequestBody CarWashDto carWash) {
        return service.create(carWash);
    }

    @PutMapping
    public CarWashDto update(@RequestBody CarWashDto carWash) {
        return service.update(carWash);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }
}
