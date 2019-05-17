package com.gliesereum.karma.controller.preference;

import com.gliesereum.karma.service.preference.ClientPreferenceService;
import com.gliesereum.share.common.model.dto.karma.preference.ClientPreferenceDto;
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
@RequestMapping("/preference")
public class ClientPreferenceController {

    @Autowired
    private ClientPreferenceService service;

    @GetMapping("/all_by_user")
    public List<ClientPreferenceDto> getAllByUser() {
        return service.getAllByUser();
    }

    @PostMapping("/by_service_id/{id}")
    public ClientPreferenceDto addPreferenceByServiceId(@PathVariable("id") UUID id) {
        return service.addPreferenceByServiceId(id);
    }

    @PostMapping("/add_list")
    public List<ClientPreferenceDto> createListByServiceIds(@Valid @RequestBody List<UUID> serviceIds) {
        return service.addListByServiceIds(serviceIds);
    }

    @DeleteMapping("/all_by_user")
    public MapResponse deleteAllByUser() {
        service.deleteAllByUser();
        return new MapResponse("true");
    }

    @DeleteMapping("/by_service_id/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.deleteByServiceId(id);
        return new MapResponse("true");
    }
}    