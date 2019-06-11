package com.gliesereum.lendinggallery.model.repository.jpa.offer.impl;

import com.gliesereum.lendinggallery.model.entity.offer.OperationsStoryEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.offer.CustomizedOperationsStoryRepository;
import com.gliesereum.share.common.model.query.lendinggallery.offer.OperationsStoryQuery;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class CustomizedOperationsStoryRepositoryImpl implements CustomizedOperationsStoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<OperationsStoryEntity> search(OperationsStoryQuery searchQuery, UUID customerId) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OperationsStoryEntity> criteriaQuery = criteriaBuilder.createQuery(OperationsStoryEntity.class);

        Root<OperationsStoryEntity> root = criteriaQuery.from(OperationsStoryEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (searchQuery != null) {
            createInIfNotEmpty(predicates, root.get("artBondId"), searchQuery.getArtBondIds());
            createInIfNotEmpty(predicates, root.get("operationType"), searchQuery.getOperationTypes());
            createBetweenDate(criteriaBuilder, predicates, root.get("create"), searchQuery.getFromDate(), searchQuery.getToDate());
        }

        createEqIfNotNull(criteriaBuilder, predicates, root.get("customerId"), customerId);

        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }

        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("create")));

        TypedQuery<OperationsStoryEntity> query = entityManager.createQuery(criteriaQuery);
        if (searchQuery != null) {
            if (searchQuery.getStart() != null) {
                query.setFirstResult(searchQuery.getStart());
            }
            if (searchQuery.getSize() != null) {
                query.setMaxResults(searchQuery.getSize());
            }
        }
        return query.getResultList();
    }

    private void createEqIfNotNull(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<?> expression, Object value) {
        if (value != null) {
            predicates.add(criteriaBuilder.equal(expression, value));
        }
    }

    private void createBetweenDate(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<? extends LocalDateTime> expression, LocalDateTime from, LocalDateTime to) {
        if (from != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression, from));
        }
        if (to != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(expression, to));
        }
    }

    private void createInIfNotEmpty(List<Predicate> predicates, Expression<?> expression, Collection<? extends Object> value) {
        if (CollectionUtils.isNotEmpty(value)) {
            predicates.add(expression.in(value));
        }
    }
}
