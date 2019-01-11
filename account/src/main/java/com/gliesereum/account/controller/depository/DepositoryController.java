package com.gliesereum.account.controller.depository;

import com.gliesereum.account.service.depository.DepositoryService;
import com.gliesereum.share.common.model.dto.account.user.DepositoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 2019-01-10
 */
@RestController
@RequestMapping("/depository")
public class DepositoryController {

    @Autowired
    private DepositoryService service;

    @GetMapping("/{ownerId}")
    public List<DepositoryDto> getByOwnerId(@PathVariable("ownerId") UUID ownerId) {
        return service.getByOwnerId(ownerId);
    }
}
