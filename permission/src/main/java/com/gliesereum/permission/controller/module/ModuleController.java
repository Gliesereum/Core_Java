package com.gliesereum.permission.controller.module;

import com.gliesereum.permission.service.module.ModuleService;
import com.gliesereum.share.common.model.dto.permission.module.ModuleDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/11/2018
 */

@RestController
@RequestMapping("/module")
public class ModuleController {

    private ModuleService moduleService;

    @Autowired
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping
    public List<ModuleDto> getAll() {
        return moduleService.getAll();
    }

    @GetMapping("/{id}")
    public ModuleDto getById(@PathVariable("id") UUID id) {
        return moduleService.getById(id);
    }

    @PostMapping
    public ModuleDto create(@Valid @RequestBody ModuleDto module) {
        return moduleService.create(module);
    }

    @PutMapping
    public ModuleDto update(@Valid @RequestBody ModuleDto module) {
        return moduleService.update(module);
    }

    @DeleteMapping
    public MapResponse delete(@RequestParam("id") UUID id) {
        moduleService.delete(id);
        return new MapResponse("success");
    }

}
