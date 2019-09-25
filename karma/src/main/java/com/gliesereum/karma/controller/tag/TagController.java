package com.gliesereum.karma.controller.tag;

import com.gliesereum.karma.service.tag.TagService;
import com.gliesereum.share.common.model.dto.karma.tag.TagDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService service;

    @GetMapping
    public List<TagDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public TagDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public TagDto create(@Valid @RequestBody TagDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public TagDto update(@Valid @RequestBody TagDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }
}