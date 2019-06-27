package com.gliesereum.karma.controller.record;

import com.gliesereum.karma.service.record.RecordMessageService;
import com.gliesereum.share.common.model.dto.karma.record.RecordMessageDto;
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
@RequestMapping("/record-message")
public class RecordMessageController {

    @Autowired
    private RecordMessageService service;

    @GetMapping
    public List<RecordMessageDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public RecordMessageDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public RecordMessageDto create(@Valid @RequestBody RecordMessageDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public RecordMessageDto update(@Valid @RequestBody RecordMessageDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }
}    