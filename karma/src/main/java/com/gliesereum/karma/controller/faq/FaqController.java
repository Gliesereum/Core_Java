package com.gliesereum.karma.controller.faq;

import com.gliesereum.karma.service.faq.FaqService;
import com.gliesereum.share.common.model.dto.karma.faq.FaqDto;
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
@RequestMapping("/faq")
public class FaqController {

    @Autowired
    private FaqService service;

    @GetMapping
    public List<FaqDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public FaqDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public FaqDto create(@Valid @RequestBody FaqDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public FaqDto update(@Valid @RequestBody FaqDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }
}    