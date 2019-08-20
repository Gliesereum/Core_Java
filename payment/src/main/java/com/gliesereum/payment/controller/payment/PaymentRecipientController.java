package com.gliesereum.payment.controller.payment;

import com.gliesereum.payment.service.payment.PaymentRecipientService;
import com.gliesereum.share.common.model.dto.payment.PaymentRecipientDto;
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
@RequestMapping("/recipient")
public class PaymentRecipientController {

    @Autowired
    private PaymentRecipientService service;

    @GetMapping
    public List<PaymentRecipientDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public PaymentRecipientDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public PaymentRecipientDto create(@Valid @RequestBody PaymentRecipientDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public PaymentRecipientDto update(@Valid @RequestBody PaymentRecipientDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }
}    