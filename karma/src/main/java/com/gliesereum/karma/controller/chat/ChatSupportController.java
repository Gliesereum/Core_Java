package com.gliesereum.karma.controller.chat;

import com.gliesereum.karma.service.chat.ChatSupportService;
import com.gliesereum.share.common.model.dto.karma.chat.ChatSupportDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@RestController
@RequestMapping("/chat-support")
public class ChatSupportController {

    @Autowired
    private ChatSupportService service;

    @GetMapping("/by-business/{id}")
    public List<ChatSupportDto> getAllByBusinessId(@PathVariable("id") UUID id) {
        return service.getByBusinessId(id);
    }

    @GetMapping("/{id}")
    public ChatSupportDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @GetMapping("/exist-chat-support")
    public boolean existChatSupport(@QueryParam("userId")UUID userId, @QueryParam("businessId")UUID businessId) {
        return service.existChatSupport(userId, businessId);
    }

    @PostMapping
    public ChatSupportDto create(@Valid @RequestBody ChatSupportDto dto) {
        return service.create(dto);
    }

    @PostMapping("/list")
    public List<ChatSupportDto> createList(@RequestBody List<ChatSupportDto> dtos) {
        return service.create(dtos);
    }

    @PutMapping
    public ChatSupportDto update(@Valid @RequestBody ChatSupportDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }
}    