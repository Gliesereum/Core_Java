package com.gliesereum.payment.controller;

import com.gliesereum.payment.service.PaymentSenderService;
import com.gliesereum.share.common.model.dto.payment.PaymentSenderDto;
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
@RequestMapping("/sender")
public class PaymentSenderController {

    @Autowired
    private PaymentSenderService service;

    @GetMapping
    public List<PaymentSenderDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public PaymentSenderDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public PaymentSenderDto create(@Valid @RequestBody PaymentSenderDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public PaymentSenderDto update(@Valid @RequestBody PaymentSenderDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }
}    