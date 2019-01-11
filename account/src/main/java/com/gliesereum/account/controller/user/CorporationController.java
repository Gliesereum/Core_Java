package com.gliesereum.account.controller.user;

import com.gliesereum.account.service.user.CorporationService;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
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
@RequestMapping("/corporation")
public class CorporationController {

    @Autowired
    private CorporationService service;

    @GetMapping
    public List<CorporationDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CorporationDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public CorporationDto create(@RequestBody CorporationDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public CorporationDto update(@RequestBody CorporationDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }

    @PostMapping("/user/{idCorporation}/{idUser}")
    public MapResponse addUserToCorporation(@PathVariable("idCorporation") UUID idCorporation, @PathVariable("idUser") UUID idUser) {
        service.addUser(idCorporation, idUser);
        return new MapResponse("true");
    }

    @DeleteMapping("/user/{idCorporation}/{idUser}")
    public MapResponse removeUserToCorporation(@PathVariable("idCorporation") UUID idCorporation, @PathVariable("idUser") UUID idUser) {
        service.removeUser(idCorporation, idUser);
        return new MapResponse("true");
    }
}
