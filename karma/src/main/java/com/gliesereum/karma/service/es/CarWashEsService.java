package com.gliesereum.karma.service.es;

import com.gliesereum.share.common.model.dto.karma.common.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.common.BusinessSearchDto;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-18
 */
public interface CarWashEsService {

    List<BaseBusinessDto> search(BusinessSearchDto businessSearch);

    void indexAll();

    void indexAllAsync();
}
