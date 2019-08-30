package com.gliesereum.karma.controller.information;

import com.gliesereum.karma.service.information.InformationService;
import com.gliesereum.share.common.model.dto.karma.information.InformationDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@RestController
@RequestMapping("/information")
public class InformationController {

    @Autowired
    private InformationService informationService;

    @GetMapping
    public List<InformationDto> getAll(@RequestParam(required = false, name = "tag") String tag,
                                       @RequestParam(required = false, name = "isoCode") String isoCode) {
        if (StringUtils.isNotBlank(tag) && StringUtils.isNotBlank(isoCode)) {
            return informationService.getByTagAndIsoCode(tag, isoCode);
        } else if (StringUtils.isNotBlank(tag)) {
            return informationService.getByTag(tag);
        } else {
            return informationService.getAll();
        }
    }

    @GetMapping("/{id}")
    public InformationDto getById(@PathVariable("id") UUID id) {
        return informationService.getById(id);
    }

    @PostMapping
    public InformationDto create(@Valid @RequestBody InformationDto dto) {
        return informationService.create(dto);
    }

    @PutMapping
    public InformationDto update(@Valid @RequestBody InformationDto dto) {
        return informationService.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        informationService.delete(id);
        return new MapResponse("true");
    }
}
