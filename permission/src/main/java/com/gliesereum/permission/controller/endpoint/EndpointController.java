package com.gliesereum.permission.controller.endpoint;

import com.gliesereum.permission.service.endpoint.EndpointService;
import com.gliesereum.share.common.model.dto.permission.endpoint.EndpointDto;
import com.gliesereum.share.common.model.dto.permission.endpoint.EndpointPackDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 04/12/2018
 */

@RestController
@RequestMapping("/endpoint")
public class EndpointController {

    @Autowired
    private EndpointService endpointService;

    @GetMapping
    public List<EndpointDto> getAll() {
        return endpointService.getAll();
    }

    @GetMapping("/{id}")
    public EndpointDto getById(@PathVariable("id") UUID id) {
        return endpointService.getById(id);
    }

    @PostMapping
    public EndpointDto create(@Valid @RequestBody EndpointDto endpoint) {
        return endpointService.create(endpoint);
    }

    @PutMapping
    public EndpointDto update (@Valid @RequestBody EndpointDto endpoint) {
        return endpointService.update(endpoint);
    }

    @DeleteMapping
    public MapResponse delete(@RequestParam("id") UUID id) {
        endpointService.delete(id);
        return new MapResponse("success");
    }

    @PostMapping("/pack")
    public List<EndpointDto> createPack(@RequestBody EndpointPackDto endpointPack) {
        return endpointService.createPack(endpointPack);
    }

}
