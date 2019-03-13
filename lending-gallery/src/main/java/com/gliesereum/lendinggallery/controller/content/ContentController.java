package com.gliesereum.lendinggallery.controller.content;

import com.gliesereum.lendinggallery.service.content.ContentService;
import com.gliesereum.share.common.model.dto.lendinggallery.content.ContentDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.ContentType;
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
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService service;

    @GetMapping
    public List<ContentDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/by-type")
    public List<ContentDto> getAllByContentType(@RequestParam(name = "type") ContentType type) {
        return service.getAllByContentType(type);
    }

    @GetMapping("/by-tags")
    public List<ContentDto> getAllByTags(@RequestBody List<String> tags) {
        return service.getAllByTags(tags);
    }

    @GetMapping("/{id}")
    public ContentDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public ContentDto create(@Valid @RequestBody ContentDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public ContentDto update(@Valid @RequestBody ContentDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }
}    