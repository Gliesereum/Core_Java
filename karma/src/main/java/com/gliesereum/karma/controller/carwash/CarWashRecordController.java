package com.gliesereum.karma.controller.carwash;

import com.gliesereum.karma.service.carwash.CarWashRecordService;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashRecordDto;
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
@RequestMapping("/record")
public class CarWashRecordController {

    @Autowired
    private CarWashRecordService service;

    @GetMapping
    public List<CarWashRecordDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CarWashRecordDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public CarWashRecordDto create(@RequestBody CarWashRecordDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public CarWashRecordDto update(@RequestBody CarWashRecordDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }
}
