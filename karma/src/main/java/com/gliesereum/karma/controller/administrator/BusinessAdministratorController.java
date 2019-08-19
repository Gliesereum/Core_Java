package com.gliesereum.karma.controller.administrator;

import com.gliesereum.karma.facade.business.BusinessPermissionFacade;
import com.gliesereum.karma.model.common.BusinessPermission;
import com.gliesereum.karma.service.administrator.BusinessAdministratorService;
import com.gliesereum.share.common.model.dto.karma.administrator.DetailedBusinessAdministratorDto;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@RestController
@RequestMapping("/business-administrator")
public class BusinessAdministratorController {

    @Autowired
    private BusinessAdministratorService businessAdministratorService;

    @Autowired
    private BusinessPermissionFacade businessPermissionFacade;

    @PostMapping
    private DetailedBusinessAdministratorDto create(@RequestParam("businessId") UUID businessId,
                                                    @RequestParam("userId") UUID userId) {
        return businessAdministratorService.create(userId, businessId);
    }

    @DeleteMapping
    private MapResponse delete(@RequestParam("businessId") UUID businessId,
                               @RequestParam("userId") UUID userId) {
        businessAdministratorService.delete(userId, businessId);
        return MapResponse.resultTrue();
    }

    @GetMapping("/by-corporation")
    public List<DetailedBusinessAdministratorDto> getByCorporation(@RequestParam("id") UUID id) {
        return businessAdministratorService.getByCorporationId(id);
    }

    @GetMapping("/by-business")
    public List<DetailedBusinessAdministratorDto> getByBusiness(@RequestParam("id") UUID id) {
        return businessAdministratorService.getByBusinessId(id);
    }

    @GetMapping("/for-business")
    private MapResponse checkCurrentUserAdmin(@RequestParam("businessId") UUID businessId) {
        SecurityUtil.checkUserByBanStatus();
        return new MapResponse(businessPermissionFacade.isHavePermissionByBusiness(businessId, BusinessPermission.VIEW_PHONE));
    }
}
