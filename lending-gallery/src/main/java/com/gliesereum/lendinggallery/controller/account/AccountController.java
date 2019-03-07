package com.gliesereum.lendinggallery.controller.account;

import com.gliesereum.lendinggallery.service.account.AccountService;
import com.gliesereum.share.common.model.dto.lendinggallery.account.AccountDto;
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
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService service;

    @GetMapping
    public List<AccountDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public AccountDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public AccountDto create(@Valid @RequestBody AccountDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public AccountDto update(@Valid @RequestBody AccountDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }
}
