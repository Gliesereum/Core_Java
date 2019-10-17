package com.gliesereum.karma.controller.location;

import com.gliesereum.karma.service.location.CityService;
import com.gliesereum.share.common.model.dto.karma.location.CityDto;
import com.gliesereum.share.common.model.dto.karma.location.GeoPositionDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/location/city")
public class CityController {

    @Autowired
    private CityService service;

    @GetMapping
    public List<CityDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CityDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public CityDto create(@Valid @RequestBody CityDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public CityDto update(@Valid @RequestBody CityDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }

    @PostMapping("add-geo-position/{id}")
    public CityDto addGeoPoints(@PathVariable("id") UUID id, @RequestBody List<GeoPositionDto> positions) {
        return service.addGeoPosition(positions, id);
    }
}