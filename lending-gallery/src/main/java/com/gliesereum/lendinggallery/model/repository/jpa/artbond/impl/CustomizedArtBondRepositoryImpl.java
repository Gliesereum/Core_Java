package com.gliesereum.lendinggallery.model.repository.jpa.artbond.impl;

import com.gliesereum.lendinggallery.model.entity.artbond.ArtBondEntity;
import com.gliesereum.lendinggallery.model.entity.offer.OperationsStoryEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.artbond.CustomizedArtBondRepository;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OperationType;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.model.query.base.OrderType;
import com.gliesereum.share.common.model.query.lendinggallery.artbond.ArtBondQuery;
import com.gliesereum.share.common.util.SqlUtil;
import org.apache.commons.collections4.CollectionUtils;

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
            Subquery<Double> subQuery = criteriaQuery.subquery(Double.class);
            Root<OperationsStoryEntity> subQueryFrom = subQuery.from(OperationsStoryEntity.class);
            Predicate artBondIdEq = criteriaBuilder.equal(subQueryFrom.get("artBondId"), root.get("id"));
            Predicate operationTypeEq = criteriaBuilder.equal(subQueryFrom.get("operationType"), OperationType.PURCHASE);
            subQuery.where(artBondIdEq, operationTypeEq);
            subQuery.select(criteriaBuilder.sum(subQueryFrom.get("sum")));


            SqlUtil.createLikeIfNotNull(criteriaBuilder, predicates, root.get("name"), queryRequest.getNameEq());
            SqlUtil.createBetweenOrGreaterOrLess(criteriaBuilder, predicates, root.get("price"), queryRequest.getPriceGreaterThan(), queryRequest.getPriceLessThan());
            SqlUtil.createBetweenOrGreaterOrLess(criteriaBuilder, predicates, root.get("stockPrice"), queryRequest.getStockPriceGreaterThan(), queryRequest.getStockPriceLessThan());
            SqlUtil.createBetweenOrGreaterOrLess(criteriaBuilder, predicates, root.get("dividendPercent"), queryRequest.getDividendPercentGreaterThan(), queryRequest.getDividendPercentLessThan());
            SqlUtil.createBetweenOrGreaterOrLess(criteriaBuilder, predicates, root.get("rewardPercent"), queryRequest.getRewardPercentGreaterThan(), queryRequest.getRewardPercentLessThan());
            SqlUtil.createBetweenOrGreaterOrLess(criteriaBuilder, predicates, subQuery, queryRequest.getAmountCollectedGreaterThan(), queryRequest.getAmountCollectedLessThan());
            SqlUtil.createEqIfNotNull(criteriaBuilder, predicates, root.get("objectState"), ObjectState.ACTIVE);

            if (CollectionUtils.isNotEmpty(predicates)) {
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
            }

            if (queryRequest.getOrderField() == null) {
                queryRequest.setOrderField("paymentStartDate");
            }
            if ((queryRequest.getOrderType() != null) && queryRequest.getOrderType().equals(OrderType.DESC)) {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get(queryRequest.getOrderField())));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get(queryRequest.getOrderField())));
            }

        }
        TypedQuery<ArtBondEntity> query = entityManager.createQuery(criteriaQuery);
        query.setMaxResults(size);
        query.setFirstResult(start);

        return query.getResultList();
    }
}
