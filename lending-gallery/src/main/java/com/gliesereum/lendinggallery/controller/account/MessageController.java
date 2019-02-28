package com.gliesereum.lendinggallery.controller.account;

import com.gliesereum.lendinggallery.service.account.MessageService;
import com.gliesereum.share.common.model.dto.lendinggallery.account.MessageDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.SectionType;
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
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService service;

    @GetMapping
    public List<MessageDto> getAll() {
        return service.getAll();
    }

    @GetMapping("by-sections")
    public List<MessageDto> getAllBySections(@RequestBody List<SectionType> types) {
        return service.getAllBySections(types);
    }

    @GetMapping("by-user")
    public List<MessageDto> getAllByUser() {
        return service.getAllByUser();
    }

    @GetMapping("/{id}")
    public MessageDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public MessageDto create(@Valid @RequestBody MessageDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public MessageDto update(@Valid @RequestBody MessageDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }
}    