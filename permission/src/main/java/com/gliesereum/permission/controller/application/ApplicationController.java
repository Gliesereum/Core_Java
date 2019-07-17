package com.gliesereum.permission.controller.application;

import com.gliesereum.permission.service.application.ApplicationService;
import com.gliesereum.share.common.model.dto.permission.application.ApplicationDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping
    public List<ApplicationDto> getAll() {
        return applicationService.getAll();
    }

    @GetMapping("/{id}")
    public ApplicationDto getById(@PathVariable("id") UUID id) {
        return applicationService.getById(id);
    }

    @PostMapping
    public ApplicationDto create(@Valid @RequestBody ApplicationDto dto) {
        return applicationService.create(dto);
    }

    @PutMapping
    public ApplicationDto update(@Valid @RequestBody ApplicationDto dto) {
        return applicationService.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        applicationService.delete(id);
        return new MapResponse("true");
    }

    @GetMapping("/check")
    public ApplicationDto check(@RequestParam("id") UUID id) {
        return applicationService.check(id);
    }
}