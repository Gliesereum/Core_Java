package com.gliesereum.lendinggallery.controller.customer;

import com.gliesereum.lendinggallery.service.customer.CustomerService;
import com.gliesereum.share.common.model.dto.lendinggallery.customer.CustomerDto;
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
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @GetMapping
    public List<CustomerDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CustomerDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @GetMapping("/user")
    public CustomerDto getByCurrentUser() {
        return service.getByUser();
    }

    @PostMapping
    public CustomerDto create(@Valid @RequestBody CustomerDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public CustomerDto update(@Valid @RequestBody CustomerDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }
}
