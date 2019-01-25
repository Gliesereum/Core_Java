package com.gliesereum.karma.controller.common;

import com.gliesereum.karma.service.common.PackageService;
import com.gliesereum.share.common.model.dto.karma.common.PackageDto;
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
@RequestMapping("/package")
public class PackageController {

    @Autowired
    private PackageService service;

    @GetMapping
    public List<PackageDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/by-business/{id}")
    public List<PackageDto> getByBusinessId(@PathVariable("id") UUID id) {
        return service.getByBusinessId(id);
    }

    @GetMapping("/{id}")
    public PackageDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public PackageDto create(@Valid @RequestBody PackageDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public PackageDto update(@Valid @RequestBody PackageDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }
}
