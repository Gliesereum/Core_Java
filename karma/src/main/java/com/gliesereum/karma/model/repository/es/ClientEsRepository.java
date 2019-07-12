package com.gliesereum.karma.model.repository.es;

import com.gliesereum.karma.model.document.ClientDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ClientEsRepository extends ElasticsearchRepository<ClientDocument, String> {
}
