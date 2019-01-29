package com.gliesereum.karma.model.repository.es;

import com.gliesereum.karma.model.document.BusinessDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface CarWashEsRepository extends ElasticsearchRepository<BusinessDocument, String> {
}
