package com.gliesereum.karma.controller.location;

import com.gliesereum.karma.service.location.DistrictService;
import com.gliesereum.share.common.model.dto.karma.location.DistrictDto;
import com.gliesereum.share.common.model.dto.karma.location.GeoPositionDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/location/district")
public class DistrictController {

    @Autowired
    private DistrictService service;

    @GetMapping
    public List<DistrictDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public DistrictDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public DistrictDto create(@Valid @RequestBody DistrictDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public DistrictDto update(@Valid @RequestBody DistrictDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }

    @PostMapping("add-geo-position/{id}")
    public DistrictDto addGeoPoints(@PathVariable("id") UUID id, @RequestBody List<GeoPositionDto> positions) {
        return service.addGeoPosition(positions, id);
    }

    @GetMapping("/by-city-id")
    public List<DistrictDto> getAllByCityId(@RequestParam(name = "cityId") UUID cityId) {
        return service.getAllByCityId(cityId);
    }
}