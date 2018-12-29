package com.gliesereum.account.controller.user;

import com.gliesereum.account.service.user.BusinessService;
import com.gliesereum.share.common.model.dto.account.user.BusinessDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @since 10/10/2018
 */
@RestController
@RequestMapping("/business")
public class BusinessController {

    @Autowired
    private BusinessService service;

    @GetMapping
    public List<BusinessDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public BusinessDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public BusinessDto create(@RequestBody BusinessDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public BusinessDto update(@RequestBody BusinessDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }

    @PostMapping("/add/{idBusiness}/{idUser}")
    public MapResponse addUserToBusiness(@PathVariable("idBusiness") UUID idBusiness, @PathVariable("idUser") UUID idUser) {
        service.addUser(idBusiness, idUser);
        return new MapResponse("true");
    }

    @PostMapping("/remove/{idBusiness}/{idUser}")
    public MapResponse removeUserToBusiness(@PathVariable("idBusiness") UUID idBusiness, @PathVariable("idUser") UUID idUser) {
        service.removeUser(idBusiness, idUser);
        return new MapResponse("true");
    }
}
