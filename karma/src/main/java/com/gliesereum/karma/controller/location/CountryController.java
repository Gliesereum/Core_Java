package com.gliesereum.karma.controller.location;

import com.gliesereum.karma.service.location.CountryService;
import com.gliesereum.share.common.model.dto.karma.location.CountryDto;
import com.gliesereum.share.common.model.dto.karma.location.GeoPositionDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/location/country")
public class CountryController {

    @Autowired
    private CountryService service;

    @GetMapping
    public List<CountryDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CountryDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public CountryDto create(@Valid @RequestBody CountryDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public CountryDto update(@Valid @RequestBody CountryDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }

    @PostMapping("add-geo-position/{id}")
    public CountryDto addGeoPoints(@PathVariable("id") UUID id, @RequestBody List<GeoPositionDto> positions) {
        return service.addGeoPosition(positions, id);
    }
}