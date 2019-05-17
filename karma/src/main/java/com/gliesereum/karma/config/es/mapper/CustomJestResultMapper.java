package com.gliesereum.karma.config.es.mapper;

import com.github.vanroy.springdata.jest.aggregation.AggregatedPage;
import com.github.vanroy.springdata.jest.aggregation.impl.AggregatedPageImpl;
import com.github.vanroy.springdata.jest.internal.ExtendedSearchResult;
import com.github.vanroy.springdata.jest.mapper.DefaultJestResultsMapper;
import com.google.gson.JsonObject;
import io.searchbox.client.JestResult;
import io.searchbox.core.SearchResult;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.ElasticsearchException;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class CustomJestResultMapper extends DefaultJestResultsMapper {

    private EntityMapper entityMapper;
    private MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext;

    public CustomJestResultMapper(EntityMapper entityMapper) {
        super(entityMapper);
        this.entityMapper = entityMapper;
    }

    public CustomJestResultMapper(MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext) {
        super(mappingContext);
        this.mappingContext = mappingContext;
    }

    public CustomJestResultMapper(MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext, EntityMapper entityMapper) {
        super(mappingContext, entityMapper);
        this.entityMapper = entityMapper;
        this.mappingContext = mappingContext;
    }

    @Override
    public <T> AggregatedPage<T> mapResults(SearchResult response, Class<T> clazz, List<AbstractAggregationBuilder> aggregations, Pageable pageable) {
        LinkedList<T> results = new LinkedList<>();

        for (SearchResult.Hit<JsonObject, Void> hit : response.getHits(JsonObject.class)) {
            if (hit != null) {
                T result = mapSource(hit.source, clazz);
                setPersistentEntityScore(result, hit.score, clazz);
                results.add(result);
            }
        }

        String scrollId = null;
        if (response instanceof ExtendedSearchResult) {
            scrollId = ((ExtendedSearchResult) response).getScrollId();
        }

        return new AggregatedPageImpl<>(results, pageable, response.getTotal(), response.getAggregations(), scrollId);
    }

    private <T> T mapSource(JsonObject source, Class<T> clazz) {
        String sourceString = source.toString();
        T result = null;
        if (!StringUtils.isEmpty(sourceString)) {
            result = mapEntity(sourceString, clazz);
            setPersistentEntityId(result, source.get(JestResult.ES_METADATA_ID).getAsString(), clazz);
        }
        return result;
    }

    private <T> T mapEntity(String source, Class<T> clazz) {
        if (!hasText(source)) {
            return null;
        }
        try {
            return entityMapper.mapToObject(source, clazz);
        } catch (IOException e) {
            throw new ElasticsearchException("failed to map source [ " + source + "] to class " + clazz.getSimpleName(), e);
        }
    }

    private <T> void setPersistentEntityId(Object entity, String id, Class<T> clazz) {

        ElasticsearchPersistentEntity<?> persistentEntity = mappingContext.getRequiredPersistentEntity(clazz);
        ElasticsearchPersistentProperty idProperty = persistentEntity.getIdProperty();

        // Only deal with text because ES generated Ids are strings !
        if ((idProperty != null) && (idProperty.getType().isAssignableFrom(String.class))) {
            persistentEntity.getPropertyAccessor(entity).setProperty(idProperty, id);
        }
    }

    private <T> void setPersistentEntityScore(T result, double score, Class<T> clazz) {
        if (clazz.isAnnotationPresent(Document.class)) {

            ElasticsearchPersistentEntity<?> entity = mappingContext.getRequiredPersistentEntity(clazz);

            if (!entity.hasScoreProperty() || (entity.getScoreProperty() == null)) {
                return;
            }
            entity.getPropertyAccessor(result)
                    .setProperty(entity.getScoreProperty(), (float) score);
        }
    }

}
