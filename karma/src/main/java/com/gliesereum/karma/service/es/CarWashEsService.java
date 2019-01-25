package com.gliesereum.karma.service.es;

import com.gliesereum.share.common.model.dto.karma.carwash.CarWashDto;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashSearchDto;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface CarWashEsService {

    List<CarWashDto> search(CarWashSearchDto carWashSearch);

    void indexAll();

    void indexAllAsync();
}
