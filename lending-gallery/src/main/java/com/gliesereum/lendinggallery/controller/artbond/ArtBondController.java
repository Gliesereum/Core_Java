package com.gliesereum.lendinggallery.controller.artbond;

import com.gliesereum.lendinggallery.service.artbond.ArtBondService;
import com.gliesereum.lendinggallery.service.media.MediaService;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.ArtBondDto;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.DetailedArtBondDto;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.InterestedArtBondDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.BlockMediaType;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.StatusType;
import com.gliesereum.share.common.model.dto.lendinggallery.media.MediaDto;
import com.gliesereum.share.common.model.dto.lendinggallery.payment.PaymentCalendarDto;
import com.gliesereum.share.common.model.query.lendinggallery.artbond.ArtBondQuery;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@RestController
@RequestMapping("/art-bond")
public class ArtBondController {

    @Autowired
    private ArtBondService service;

    @Autowired
    private MediaService mediaService;

    @GetMapping
    public List<ArtBondDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ArtBondDto getById(@PathVariable("id") UUID id) {
        return service.getArtBondById(id);
    }

    @PostMapping
    public ArtBondDto create(@Valid @RequestBody ArtBondDto dto) {
        return service.create(dto);
    }

    @PutMapping
    public ArtBondDto update(@Valid @RequestBody ArtBondDto dto) {
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return new MapResponse("true");
    }

    @PostMapping("/search")
    public List<ArtBondDto> search(@RequestBody(required = false) ArtBondQuery searchQuery) {
        return service.search(searchQuery);
    }

    @PostMapping("/by-tags")
    public List<ArtBondDto> getAllByTags(@RequestBody List<String> tags) {
        return service.getAllByTags(tags);
    }

    @GetMapping("/by-tags")
    public List<ArtBondDto> getAllByTag(@RequestParam("tags") List<String> tags) {
        return service.getAllByTags(tags);
    }

    @GetMapping("/status")
    public List<ArtBondDto> getAllByStatus(@RequestParam("status") StatusType status) {
        return service.getAllByStatus(status);
    }

    @GetMapping("/detailed/all")
    public List<DetailedArtBondDto> getDetailedAll() {
        return service.getDetailedAll();
    }

    @GetMapping("/by-user")
    public List<ArtBondDto> getAllByUser() {
        return service.getAllByUser();
    }

    @PutMapping("/status")
    public ArtBondDto updateStatus(@RequestParam("status") StatusType status,
                                   @RequestParam("id") UUID id) {
        return service.updateStatus(status, id);
    }

    @GetMapping("/media/by-block-type")
    public List<MediaDto> getFilesByBlockType(@RequestParam("objectId") UUID objectId,
                                              @RequestParam("blockMediaType") BlockMediaType blockMediaType) {
        return mediaService.getByObjectIdAndType(objectId, blockMediaType);
    }

    @PostMapping("/media")
    public MediaDto create(@RequestBody @Valid MediaDto media) {
        return mediaService.create(media);
    }

    @PostMapping("/list/media/{id}")
    public ArtBondDto createList(@PathVariable("id") UUID id, @RequestBody List<MediaDto> files) {
        return mediaService.createList(files, id);
    }

    @PutMapping("/media")
    public MediaDto update(@RequestBody @Valid MediaDto media) {
        return mediaService.update(media);
    }

    @DeleteMapping("/{id}/media/{mediaId}")
    public MapResponse delete(@PathVariable("id") UUID id, @PathVariable("mediaId") UUID mediaId) {
        mediaService.delete(mediaId, id);
        return new MapResponse("true");
    }

    @GetMapping("/currency-exchange")
    public Map<String, Double> currencyExchange(@RequestParam("sum") Long sum) {
        return service.currencyExchange(sum);
    }

    @GetMapping("/{id}/payment-calendar")
    public List<PaymentCalendarDto> getPaymentCalendar(@PathVariable UUID id) {
        return service.getPaymentCalendar(id, false);
    }

    @GetMapping("/{id}/nkd")
    public MapResponse getNkdByArtBond(@PathVariable UUID id) {
        Double nkd = service.getNkd(id);
        MapResponse response = null;
        if (nkd != null) {
            response = new MapResponse("nkd", nkd);
        }
        return response;
    }

    @GetMapping("/{id}/percent-per-year")
    public MapResponse percentPerYear(@PathVariable UUID id) {
        Map<String, Integer> result = service.getPercentPerYear(id);
        MapResponse response = null;
        if (result != null) {
            response = new MapResponse(result);
        }
        return response;
    }

    @GetMapping("/{id}/amount-collected")
    public MapResponse getAmountCollected(@PathVariable UUID id) {
        Double result = service.getAmountCollected(id);
        MapResponse response = null;
        if (result != null) {
            response = new MapResponse("amountCollected", result);
        }
        return response;
    }

    @PostMapping("/interested/{id}")
    public InterestedArtBondDto interested(@PathVariable("id") UUID id) {
        return service.interested(id);
    }

    @PostMapping("/not-interested/{id}")
    public MapResponse notInterested(@PathVariable("id") UUID id) {
        service.notInterested(id);
        return new MapResponse("true");
    }

    @GetMapping("/get-interested/{id}")
    public List<InterestedArtBondDto> getInterested(@PathVariable("id") UUID id) {
        return service.getInterested(id);
    }

    @GetMapping("/by-current-advisor")
    public List<ArtBondDto> getAllByAdvisorCurrent() {
        return service.getAllByCurrentAdvisor();
    }

    @GetMapping("/by-advisor-id")
    public List<ArtBondDto> getAllByAdvisorId(@RequestParam("id") UUID id) {
        return service.getAllByAdvisor(id);
    }
}
