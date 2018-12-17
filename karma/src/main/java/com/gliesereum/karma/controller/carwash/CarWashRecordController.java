package com.gliesereum.karma.controller.carwash;

import com.gliesereum.karma.service.carwash.CarWashRecordService;
import com.gliesereum.karma.service.carwash.CarWashService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashRecordDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.CARWASH_ID_EMPTY;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.DONT_HAVE_PERMISSION_TO_ACTION_CARWASH;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@RestController
@RequestMapping("/record")
public class CarWashRecordController {

    @Autowired
    private CarWashRecordService service;

    @Autowired
    private CarWashService carWashService;

    @GetMapping
    public List<CarWashRecordDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CarWashRecordDto getById(@PathVariable("id") UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public CarWashRecordDto create(@RequestBody CarWashRecordDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public CarWashRecordDto update(@RequestBody CarWashRecordDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }

    @PostMapping("/client/params")
    public List<CarWashRecordDto> getByParamsForClient(@RequestBody Map<String, String> params) {
        return service.getByParamsForClient(params);
    }

    @PostMapping("/business/params")
    public List<CarWashRecordDto> getByParamsForBusiness(@RequestBody Map<String, String> params) {
        if (StringUtils.isNotEmpty(params.get("carWashId"))) {
            if (!carWashService.currentUserHavePermissionToAction(UUID.fromString(params.get("carWashId")))) {
                throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_CARWASH);
            }
        } else {
            throw new ClientException(CARWASH_ID_EMPTY);
        }
        return service.getByParamsForBusiness(params);
    }

    @PostMapping("/map/params")
    public List<CarWashRecordDto> getByParamsForMap(@RequestBody Map<String, String> params) {
        if (StringUtils.isEmpty(params.get("carWashId"))) {
            throw new ClientException(CARWASH_ID_EMPTY);
        }
        return service.getByParamsForBusiness(params);
    }
}