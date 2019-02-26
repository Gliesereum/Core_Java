package com.gliesereum.account.controller.user;

import com.gliesereum.account.service.user.KycService;
import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 */
@RestController
@RequestMapping("/kyc")
public class KycController {

    @Autowired
    private KycService service;

    @PostMapping("/status")
    public MapResponse updateKyc(@RequestParam(value = "status") KYCStatus status,
                                 @RequestParam(value = "isCorporation") boolean isCorporation,
                                 @RequestParam(value = "objectId") UUID objectId) {
        service.updateKycStatus(status, objectId, isCorporation);
        return new MapResponse("true");
    }
    //TODO: KYC REFACTOR
//    @PostMapping("/upload-document")
//    public MapResponse uploadDocument(@RequestParam("file") MultipartFile file,
//                                      @RequestParam(value = "isCorporation") boolean isCorporation,
//                                      @RequestParam(value = "objectId") UUID objectId) {
//        service.uploadDocument(file, objectId, isCorporation);
//        return new MapResponse("true");
//    }

//    @DeleteMapping("/delete-document")
//    public MapResponse deleteDocument(@RequestParam("path") String path,
//                                      @RequestParam(value = "isCorporation") boolean isCorporation,
//                                      @RequestParam(value = "objectId") UUID objectId) {
//        service.deleteDocument(path, objectId, isCorporation);
//        return new MapResponse("true");
//    }

//    @GetMapping("/corporation/request")
//    public List<CorporationDto> getCorporationRequest() {
//        return service.getAllCorporationKycRequest();
//    }

//    @GetMapping("/user/request")
//    public List<UserDto> getUserRequest() {
//        return service.getAllUserKycRequest();
//    }
}
