package com.gliesereum.language.controller;

import com.gliesereum.language.service.PackageService;
import com.gliesereum.share.common.model.dto.language.PackageDto;
import com.gliesereum.share.common.model.dto.language.PackageMapDto;
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
@RequestMapping("/package")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @GetMapping
    public List<PackageDto> getAll() {
        return packageService.getAll();
    }

    @GetMapping("/by-module")
    public List<PackageDto> getByModule(@RequestParam("module") String module) {
        return packageService.getByModule(module);
    }

    @GetMapping("/map/by-module")
    public PackageMapDto getMapByModule(@RequestParam("module") String module) {
        return packageService.getMapByModule(module);
    }

    @PostMapping
    public PackageDto create(@RequestParam(name = "create-from", required = false) UUID createFrom,
                             @Valid @RequestBody PackageDto packageDto) {
        return packageService.create(packageDto, createFrom);
    }

    @PutMapping
    public PackageDto update(@Valid @RequestBody PackageDto packageDto) {
        return packageService.update(packageDto);
    }
}
