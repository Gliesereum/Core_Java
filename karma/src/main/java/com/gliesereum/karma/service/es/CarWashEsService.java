package com.gliesereum.karma.service.es;

import com.gliesereum.share.common.model.dto.karma.common.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.common.BusinessSearchDto;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface CarWashEsService {

    List<BaseBusinessDto> search(BusinessSearchDto businessSearch);

    void indexAll();

    void indexAllAsync();
}
