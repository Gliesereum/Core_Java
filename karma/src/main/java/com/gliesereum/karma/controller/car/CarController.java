package com.gliesereum.karma.controller.car;

import com.gliesereum.karma.service.car.BrandCarService;
import com.gliesereum.karma.service.car.CarService;
import com.gliesereum.karma.service.car.ModelCarService;
import com.gliesereum.karma.service.car.YearCarService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.car.BrandCarDto;
import com.gliesereum.share.common.model.dto.karma.car.CarDto;
import com.gliesereum.share.common.model.dto.karma.car.ModelCarDto;
import com.gliesereum.share.common.model.dto.karma.car.YearCarDto;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.util.SecurityUtil;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.USER_IS_ANONYMOUS;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
@RestController
@RequestMapping("/car")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private BrandCarService brandCarService;

    @Autowired
    private ModelCarService modelCarService;

    @Autowired
    private YearCarService yearCarService;

    @GetMapping("/{id}")
    public CarDto getById(@PathVariable("id") UUID id) {
        return carService.getById(id);
    }

    @GetMapping("/user")
    public List<CarDto> getAll() {
        UUID userId = SecurityUtil.getUserId();
        if (userId == null) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return ListUtils.emptyIfNull(carService.getAllByUserId(userId));
    }

    @PostMapping
    public CarDto create(@RequestBody CarDto car) {
        return carService.create(car);
    }

    @PostMapping("/service/{idCar}/{idService}")
    public CarDto addService(@PathVariable("idCar") UUID idCar, @PathVariable("idService") UUID idService) {
        return carService.addService(idCar, idService);
    }

    @DeleteMapping("/remove/service/{idCar}/{idService}")
    public CarDto removeService(@PathVariable("idCar") UUID idCar, @PathVariable("idService") UUID idService) {
        return carService.removeService(idCar, idService);
    }

    @PutMapping
    public CarDto update(@Valid @RequestBody CarDto car) {
        return carService.update(car);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        carService.delete(id);
        return new MapResponse("true");
    }

    @DeleteMapping("/all-by-user")
    public MapResponse deleteByUserId() {
        carService.deleteByUserId(SecurityUtil.getUserId());
        //TODO: notify other service
        return new MapResponse("true");
    }

    @GetMapping("/brands")
    public List<BrandCarDto> getAllBrands() {
        return brandCarService.getAll();
    }

    @GetMapping("/models")
    public List<ModelCarDto> getAllModels() {
        return modelCarService.getAll();
    }

    @GetMapping("/models/by-brand/{id}")
    public List<ModelCarDto> getAllModelsByBrandId(@PathVariable("id") UUID id) {
        return modelCarService.getAllByBrandId(id);
    }

    @GetMapping("/years")
    public List<YearCarDto> getAllYears() {
        return yearCarService.getAll();
    }
}
