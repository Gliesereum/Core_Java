package com.gliesereum.account.controller.kyc;

import com.gliesereum.account.service.kyc.KycFieldService;
import com.gliesereum.account.service.kyc.KycRequestService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.enumerated.KycRequestType;
import com.gliesereum.share.common.model.dto.account.enumerated.KycStatus;
import com.gliesereum.share.common.model.dto.account.kyc.KycValuesRequestDto;
import com.gliesereum.share.common.model.dto.account.kyc.KycFieldDto;
import com.gliesereum.share.common.model.dto.account.kyc.KycRequestDto;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.USER_IS_ANONYMOUS;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@RestController
@RequestMapping("/kyc")
public class KycController {

    @Autowired
    private KycFieldService kycFieldService;

    @Autowired
    private KycRequestService kycRequestService;

    @GetMapping("/field")
    public List<KycFieldDto> getFields(@RequestParam(required = false, name = "type") KycRequestType type) {
        return kycFieldService.getByType(type);
    }

    @PostMapping("/field")
    public KycFieldDto createField(@Valid @RequestBody KycFieldDto kycField) {
        return kycFieldService.create(kycField);
    }

    @PutMapping("/field")
    public KycFieldDto updateField(@Valid @RequestBody KycFieldDto kycField) {
        return kycFieldService.update(kycField);
    }

    @DeleteMapping("/field/{id}")
    public MapResponse removeField(@PathVariable UUID id) {
        kycFieldService.delete(id);
        return MapResponse.resultTrue();
    }

    @PostMapping("/request")
    public KycRequestDto createRequest(@RequestBody KycValuesRequestDto createRequest) {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return kycRequestService.createRequest(createRequest);
    }

    @PutMapping("/request")
    public KycRequestDto updateRequest(@RequestBody KycValuesRequestDto updateRequest) {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return kycRequestService.updateRequest(updateRequest);
    }

    @PutMapping("/request/status")
    public KycRequestDto updateStatus(@RequestParam("id") UUID id,
                                      @RequestParam("status") KycStatus kycStatus,
                                      @RequestParam(name = "comment", required = false) String comment) {
        return kycRequestService.updateStatus(id, kycStatus, comment);
    }

    @GetMapping("/request/user")
    public List<KycRequestDto> getAllUserRequest() {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return kycRequestService.getAllByUserId(SecurityUtil.getUserId());
    }

    @GetMapping("/request/all")
    public List<KycRequestDto> getAllRequest() {
        return kycRequestService.getAll();
    }
}
