package com.gliesereum.karma.controller.service;

import com.gliesereum.karma.service.service.PackageService;
import com.gliesereum.share.common.model.dto.karma.service.PackageDto;
import com.gliesereum.share.common.model.dto.karma.tag.TagDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
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

    @PostMapping("/tag/add")
    public List<TagDto> addTag(@RequestParam(value = "tagId") UUID tagId,
                               @RequestParam(value = "idPackage") UUID idPackage) {
        return service.addTag(tagId, idPackage);
    }

    @GetMapping("/{id}/tags")
    public List<TagDto> getTags(@PathVariable("id") UUID idPackage) {
        return service.getTags(idPackage);
    }

    @PostMapping("/tag/save")
    public List<TagDto> saveTag(@RequestParam(value = "tagId") List<UUID> tagId,
                                @RequestParam(value = "idPackage") UUID idPackage) {
        return service.saveTags(tagId, idPackage);
    }

    @PostMapping("/tag/remove")
    public List<TagDto> removeTag(@RequestParam(value = "tagId") UUID tagId,
                                  @RequestParam(value = "idPackage") UUID idPackage) {
        return service.removeTag(tagId, idPackage);
    }

}
