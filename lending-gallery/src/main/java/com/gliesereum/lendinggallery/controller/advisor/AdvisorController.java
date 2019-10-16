package com.gliesereum.lendinggallery.controller.advisor;

import com.gliesereum.lendinggallery.service.advisor.AdvisorService;
import com.gliesereum.share.common.model.dto.lendinggallery.advisor.AdvisorDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/advisor")
public class AdvisorController {
	
	@Autowired
	private AdvisorService advisorService;
	
	@GetMapping
	public Page<AdvisorDto> getAll(@PageableDefault(page = 0, size = 100, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
		return advisorService.getAll(ObjectState.ACTIVE, pageable);
	}
	
	@GetMapping("/{id}")
	public AdvisorDto getById(@PathVariable("id") UUID id) {
		return advisorService.getById(id);
	}
	
	@PostMapping
	public AdvisorDto create(@Valid @RequestBody AdvisorDto dto) {
		return advisorService.create(dto);
	}
	
	@PutMapping
	public AdvisorDto update(@Valid @RequestBody AdvisorDto dto) {
		return advisorService.update(dto);
	}
	
	@DeleteMapping("/{id}")
	public MapResponse delete(@PathVariable("id") UUID id) {
		advisorService.delete(id);
		return new MapResponse("true");
	}
	
	@PostMapping("/create-with-user")
	public AdvisorDto createWorkerWithUser(@RequestBody @Valid AdvisorDto advisor) {
		return advisorService.createWithUser(advisor);
	}
	
	@GetMapping("/by-art-bond")
	public Page<AdvisorDto> getByBusinessId(@RequestParam("artBondId") UUID artBondId,
	                                        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
	                                        @RequestParam(value = "size", required = false, defaultValue = "100") Integer size) {
		return advisorService.getByArtBondId(artBondId, true, page, size);
	}
	
	@GetMapping("/exist/byPhone")
	public MapResponse checkAdvisorExistByPhone(@RequestParam("phone") String phone) {
		Boolean exist = advisorService.checkAdvisorExistByPhone(phone);
		return new MapResponse("exist", exist);
	}
}
