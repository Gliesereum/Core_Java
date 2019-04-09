package com.gliesereum.lendinggallery.model.repository.jpa.artbond.impl;

import com.gliesereum.lendinggallery.model.entity.artbond.ArtBondEntity;
import com.gliesereum.lendinggallery.model.entity.offer.OperationsStoryEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.artbond.CustomizedArtBondRepository;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OperationType;
import com.gliesereum.share.common.model.query.base.OrderType;
import com.gliesereum.share.common.model.query.lendinggallery.artbond.ArtBondQuery;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class CustomizedArtBondRepositoryImpl implements CustomizedArtBondRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ArtBondEntity> search(ArtBondQuery queryRequest) {
        int start = 0;
        int size = 100;

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ArtBondEntity> criteriaQuery = criteriaBuilder.createQuery(ArtBondEntity.class);

        Root<ArtBondEntity> root = criteriaQuery.from(ArtBondEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (queryRequest != null) {
            if (queryRequest.getFrom() != null) {
                start = queryRequest.getFrom();
            }
            if (queryRequest.getSize() != null) {
                size = queryRequest.getSize();
            }
            createEqIfNotNull(criteriaBuilder, predicates, root.get("name"), queryRequest.getNameEq());
            createGreaterThanIfNotNull(criteriaBuilder, predicates, root.get("price"), queryRequest.getPriceGreaterThan());
            createLessThanIfNotNull(criteriaBuilder, predicates, root.get("price"), queryRequest.getPriceLessThan());
            createGreaterThanIfNotNull(criteriaBuilder, predicates, root.get("stockPrice"), queryRequest.getStockPriceGreaterThan());
            createLessThanIfNotNull(criteriaBuilder, predicates, root.get("stockPrice"), queryRequest.getStockPriceLessThan());
            createGreaterThanIfNotNull(criteriaBuilder, predicates, root.get("dividendPercent"), queryRequest.getDividendPercentGreaterThan());
            createLessThanIfNotNull(criteriaBuilder, predicates, root.get("dividendPercent"), queryRequest.getDividendPercentLessThan());
            createGreaterThanIfNotNull(criteriaBuilder, predicates, root.get("rewardPercent"), queryRequest.getRewardPercentGreaterThan());
            createLessThanIfNotNull(criteriaBuilder, predicates, root.get("rewardPercent"), queryRequest.getRewardPercentLessThan());

            Subquery<Double> subQuery = criteriaQuery.subquery(Double.class);
            Root<OperationsStoryEntity> subQueryFrom = subQuery.from(OperationsStoryEntity.class);
            Predicate artBondIdEq = criteriaBuilder.equal(subQueryFrom.get("artBondId"), root.get("id"));
            Predicate operationTypeEq = criteriaBuilder.equal(subQueryFrom.get("operationType"), OperationType.PURCHASE);
            subQuery.where(artBondIdEq, operationTypeEq);
            subQuery.select(criteriaBuilder.sum(subQueryFrom.get("sum")));

            if (queryRequest.getAmountCollectedGreaterThan() != null) {
                predicates.add(criteriaBuilder.greaterThan(subQuery, queryRequest.getAmountCollectedGreaterThan()));
            }
            if (queryRequest.getAmountCollectedLessThan() != null) {
                predicates.add(criteriaBuilder.lessThan(subQuery, queryRequest.getAmountCollectedLessThan()));
            }
            if (CollectionUtils.isNotEmpty(predicates)) {
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
            }
            if (queryRequest.getOrderField() == null) {
                queryRequest.setOrderField("paymentStartDate");
            }
            if ((queryRequest.getOrderType() != null) && queryRequest.getOrderType().equals(OrderType.DESC)) {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get(queryRequest.getOrderField())));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get(queryRequest.getOrderField())));
            }

        }
        TypedQuery<ArtBondEntity> query = entityManager.createQuery(criteriaQuery);
        query.setMaxResults(size);
        query.setFirstResult(start);

        return query.getResultList();
    }

    private void createEqIfNotNull(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<?> expression, Object value) {
        if (value != null) {
            predicates.add(criteriaBuilder.equal(expression, value));
        }
    }

    private void createGreaterThanIfNotNull(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<? extends Number> expression, Number value) {
        if (value != null) {
            predicates.add(criteriaBuilder.gt(expression, value));
        }
    }

    private void createLessThanIfNotNull(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<? extends Number> expression, Number value) {
        if (value != null) {
            predicates.add(criteriaBuilder.lt(expression, value));
        }
    }
}
