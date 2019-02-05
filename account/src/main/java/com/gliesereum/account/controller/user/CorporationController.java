package com.gliesereum.account.controller.user;

import com.gliesereum.account.service.user.CorporationService;
import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.account.user.CorporationSharedOwnershipDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/kyc/status")
    public MapResponse updateKyc(@RequestParam(value = "status") KYCStatus status,
                                 @RequestParam(value = "corporationId") UUID corporationId) {
        service.updateKycStatus(status,corporationId);
        return new MapResponse("true");
    }

    @PostMapping("/kyc/upload-document")
    public MapResponse uploadDocument(@RequestParam("file") MultipartFile file,
                                      @RequestParam(value = "corporationId") UUID corporationId) {
        service.uploadDocument(file, corporationId);
        return new MapResponse("true");
    }

    @DeleteMapping("/kyc/delete-document")
    public MapResponse deleteDocument(@RequestParam("path") String path,
                                      @RequestParam(value = "idCorporation") UUID idCorporation) {
        service.deleteDocument(path, idCorporation);
        return new MapResponse("true");
    }

    @GetMapping("/kyc/request")
    public List<CorporationDto> getRequest() {
        return service.getAllRequest();
    }
}
