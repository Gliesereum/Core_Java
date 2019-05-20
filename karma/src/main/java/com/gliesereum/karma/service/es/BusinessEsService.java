package com.gliesereum.karma.service.es;

import com.gliesereum.karma.model.document.BusinessDocument;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.BusinessSearchDto;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface BusinessEsService {

    List<BaseBusinessDto> search(BusinessSearchDto businessSearch);

    List<BusinessDocument> searchDocuments(BusinessSearchDto businessSearch);

    void indexAll();

    void indexAllAsync();
}
