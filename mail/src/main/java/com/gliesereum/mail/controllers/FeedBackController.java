package com.gliesereum.mail.controllers;

import com.gliesereum.mail.service.feedback.FeedBackRequestService;
import com.gliesereum.mail.service.feedback.FeedBackUserService;
import com.gliesereum.share.common.model.dto.mail.FeedBackRequestDto;
import com.gliesereum.share.common.model.dto.mail.FeedBackUserDto;
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
@RequestMapping("/feed-back")
public class FeedBackController {

    @Autowired
    private FeedBackRequestService requestService;

    @Autowired
    private FeedBackUserService service;

    @GetMapping("/user/by-app/{id}")
    public List<FeedBackUserDto> getAllByAppId(@PathVariable("id") UUID id) {
        return service.getAllByAppId(id);
    }

    @GetMapping("/user/{id}")
    public FeedBackUserDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping("/user")
    public FeedBackUserDto create(@RequestBody FeedBackUserDto dto) {
        return service.create(dto);
    }

    @PostMapping("/user/list")
    public List<FeedBackUserDto> createList(@RequestBody List<FeedBackUserDto> dtos) {
        return service.createList(dtos);
    }

    @DeleteMapping("/user/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }

    @PostMapping("/request")
    public FeedBackRequestDto create(@Valid @RequestBody FeedBackRequestDto dto) {
        return requestService.create(dto);
    }
}    