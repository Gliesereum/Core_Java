package com.gliesereum.karma.model.repository.es;

import com.gliesereum.karma.model.document.CarWashDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface CarWashEsRepository extends ElasticsearchRepository<CarWashDocument, String> {
}
