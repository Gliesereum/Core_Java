package com.gliesereum.account.controller.user;

import com.gliesereum.account.service.user.CorporationEmployeeService;
import com.gliesereum.account.service.user.CorporationService;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.account.user.CorporationEmployeeDto;
import com.gliesereum.share.common.model.dto.account.user.CorporationSharedOwnershipDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 */
@RestController
@RequestMapping("/corporation")
public class CorporationController {

    @Autowired
    private CorporationService service;

    @Autowired
    private CorporationEmployeeService employeeService;

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

    @PostMapping("/owner")
    public MapResponse addOwnerCorporation(@Valid @RequestBody CorporationSharedOwnershipDto dto) {
        service.addOwnerCorporation(dto);
        return new MapResponse("true");
    }

    @DeleteMapping("/owner/{id}")
    public MapResponse removeOwnerCorporation(@PathVariable("id") UUID id) {
        service.removeOwnerCorporation(id);
        return new MapResponse("true");
    }

    @GetMapping("/employee/{corporationId}")
    public List<CorporationEmployeeDto> employeeGetAll(@PathVariable("corporationId") UUID corporationId) {
        return employeeService.getAllByCorporationId(corporationId);
    }

    @GetMapping("/employee/{id}")
    public CorporationEmployeeDto employeeGetById(@PathVariable("id") UUID id) {
        return employeeService.getById(id);
    }

    @PostMapping("/employee")
    public CorporationEmployeeDto employeeCreate(@RequestBody CorporationEmployeeDto dto) {
        return employeeService.create(dto);
    }

    @PutMapping("/employee")
    public CorporationEmployeeDto employeeUpdate(@RequestBody CorporationEmployeeDto dto) {
        return employeeService.update(dto);
    }

    @DeleteMapping("/employee/{id}")
    public MapResponse employeeDelete(@PathVariable("id") UUID id) {
        employeeService.delete(id);
        return new MapResponse("true");
    }
}
