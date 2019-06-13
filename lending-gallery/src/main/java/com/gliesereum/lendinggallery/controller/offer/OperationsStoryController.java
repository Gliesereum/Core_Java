package com.gliesereum.lendinggallery.controller.offer;

import com.gliesereum.lendinggallery.service.offer.OperationsStoryService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OperationType;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OperationsStoryDto;
import com.gliesereum.share.common.model.query.lendinggallery.offer.OperationsStoryQuery;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.USER_IS_ANONYMOUS;

/**
 * @author vitalij
 * @version 1.0
 */
@RestController
@RequestMapping("/operations-story")
public class OperationsStoryController {

    @Autowired
    private OperationsStoryService service;

    @GetMapping
    public List<OperationsStoryDto> getAll() {
        return service.getAll();
    }

    @GetMapping("by-user")
    public List<OperationsStoryDto> getAllByUser() {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return service.getAllByUserId(SecurityUtil.getUserId());
    }

    @PostMapping("/by-user/filter")
    public List<OperationsStoryDto> filterByUser(@RequestBody(required = false) OperationsStoryQuery operationsStoryQuery) {
        SecurityUtil.checkUserByBanStatus();
        return service.filterByUserId(operationsStoryQuery, SecurityUtil.getUserId());
    }

    @GetMapping("by-customer/{customerId}")
    public List<OperationsStoryDto> getAllByCustomer(@PathVariable("customerId") UUID customerId) {
        return service.getAllByCustomerId(customerId);
    }

    @GetMapping("by-user/by-art-bond/{artBondId}")
    public List<OperationsStoryDto> getAllForUserByArtBondId(@PathVariable("artBondId") UUID artBondId) {
        return service.getAllForUserByArtBondId(artBondId);
    }

    @GetMapping("/by-art-bond")
    public List<OperationsStoryDto> getByArtBond(@RequestParam("artBondId") UUID artBondId, @RequestParam("operationType") OperationType operationType) {
        return service.getByArtBondIdAndOperationType(artBondId, operationType);
    }

    @GetMapping("/{id}")
    public OperationsStoryDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public OperationsStoryDto create(@Valid @RequestBody OperationsStoryDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public OperationsStoryDto update(@Valid @RequestBody OperationsStoryDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }
}    