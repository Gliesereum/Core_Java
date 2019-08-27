package com.gliesereum.lendinggallery.model.repository.jpa.offer.impl;

import com.gliesereum.lendinggallery.model.entity.offer.OperationsStoryEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.offer.CustomizedOperationsStoryRepository;
import com.gliesereum.share.common.model.query.lendinggallery.offer.OperationsStoryQuery;
import com.gliesereum.share.common.util.SqlUtil;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
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
            SqlUtil.createInIfNotEmpty(predicates, root.get("artBondId"), searchQuery.getArtBondIds());
            SqlUtil.createInIfNotEmpty(predicates, root.get("operationType"), searchQuery.getOperationTypes());
            SqlUtil.createBetweenDate(criteriaBuilder, predicates, root.get("create"), searchQuery.getFromDate(), searchQuery.getToDate());
        }

        SqlUtil.createEqIfNotNull(criteriaBuilder, predicates, root.get("customerId"), customerId);

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

}
