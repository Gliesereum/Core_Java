package com.gliesereum.lendinggallery.controller.offer;

import com.gliesereum.lendinggallery.service.offer.BorrowerOfferService;
import com.gliesereum.lendinggallery.service.offer.InvestorOfferService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OfferStateType;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.BorrowerOfferDto;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.InvestorOfferDto;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.InvestorOfferFullModelDto;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OfferSearchDto;
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
@RequestMapping("/offer")
public class OfferController {

    @Autowired
    private InvestorOfferService investorOfferService;

    @Autowired
    private BorrowerOfferService borrowerOfferService;

    @GetMapping("/investor")
    public List<InvestorOfferDto> getAllInvestorOffers() {
        return investorOfferService.getAll();
    }

    @GetMapping("/art-bond")
    public List<InvestorOfferDto> getAllOffersByArtBond(@RequestParam("id") UUID id) {
        return investorOfferService.getAllByArtBond(id);
    }

    @GetMapping("/investor/by-state")
    public List<InvestorOfferDto> getAllInvestorOffersByState(@RequestParam("state") OfferStateType state) {
        return investorOfferService.getAllByState(state);
    }

    @GetMapping("/investor/full-model/by-state")
    public List<InvestorOfferFullModelDto> getAllInvestorOffersFullModelByState(@RequestParam("state") OfferStateType state) {
        return investorOfferService.getAllFullModelByState(state);
    }

    @PostMapping("/investor/full-model/by-current-adviser")
    public List<InvestorOfferFullModelDto> searchInvestorOffersFullModelByCurrentAdviser(@RequestBody OfferSearchDto search) {
        return investorOfferService.searchInvestorOffersFullModelByCurrentAdviser(search);
    }

    @GetMapping("/investor/user")
    public List<InvestorOfferDto> getAllInvestorOffersByUser() {
        return investorOfferService.getAllByUser();
    }

    @GetMapping("/investor/{id}")
    public InvestorOfferDto getByIdInvestorOffer(@PathVariable("id") UUID id) {
        return investorOfferService.getById(id);
    }

    @PostMapping("/investor")
    public InvestorOfferDto createInvestorOffer(@Valid @RequestBody InvestorOfferDto dto) {
        return investorOfferService.create(dto);
    }

    @PutMapping("/investor")
    public InvestorOfferDto updateInvestorOffer(@Valid @RequestBody InvestorOfferDto dto) {
        return investorOfferService.update(dto);
    }

    @PutMapping("/investor/state")
    public InvestorOfferDto updateInvestorOfferState(@RequestParam("state") OfferStateType state,
                                                     @RequestParam("id") UUID id,
                                                     @RequestParam(name = "comment", required = false) String comment) {
        return investorOfferService.updateState(state, id, comment);
    }

    @PostMapping("/add-comment")
    public InvestorOfferDto setComment(@RequestParam("id") UUID id, @RequestParam("comment") String comment) {
        return investorOfferService.setComment(id, comment);
    }

    @GetMapping("/borrower")
    public List<BorrowerOfferDto> getAllBorrowerOffers() {
        return borrowerOfferService.getAll();
    }

    @GetMapping("/borrower/by-state")
    public List<BorrowerOfferDto> getAllBorrowerOffersByState(@RequestParam("state") OfferStateType state) {
        return borrowerOfferService.getAllByState(state);
    }

    @GetMapping("/borrower/user")
    public List<BorrowerOfferDto> getAllBorrowerOffersByUser() {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return borrowerOfferService.getAllByUser();
    }

    @GetMapping("/borrower/{id}")
    public BorrowerOfferDto getByIdBorrowerOffer(@PathVariable("id") UUID id) {
        return borrowerOfferService.getById(id);
    }

    @PostMapping("/borrower")
    public BorrowerOfferDto createBorrowerOffer(@Valid @RequestBody BorrowerOfferDto dto) {
        return borrowerOfferService.create(dto);
    }

    @PutMapping("/borrower/state")
    public BorrowerOfferDto updateBorrowerOfferState(@RequestParam("state") OfferStateType state,
                                                     @RequestParam("id") UUID id) {
        return borrowerOfferService.updateState(state, id);
    }
}    