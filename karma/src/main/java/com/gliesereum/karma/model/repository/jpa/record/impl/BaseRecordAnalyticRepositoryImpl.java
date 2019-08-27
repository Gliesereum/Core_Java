package com.gliesereum.karma.model.repository.jpa.record.impl;

import com.gliesereum.karma.model.entity.record.BaseRecordEntity;
import com.gliesereum.karma.model.repository.jpa.record.BaseRecordAnalyticRepository;
import com.gliesereum.share.common.model.dto.karma.analytics.AnalyticFilterDto;
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

/**
 * @author vitalij
 * @version 1.0
 */
public class BaseRecordAnalyticRepositoryImpl implements BaseRecordAnalyticRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BaseRecordEntity> getRecordsByFilter(AnalyticFilterDto filter) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BaseRecordEntity> query = builder.createQuery(BaseRecordEntity.class);
        Root<BaseRecordEntity> root = query.from(BaseRecordEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        SqlUtil.createBetweenOrGreaterOrLess(builder, predicates, root.get("price"), filter.getPriceMin(), filter.getPriceMax());
        SqlUtil.createBetweenDate(builder, predicates, root.get("begin"), filter.getFrom(), filter.getTo());
        SqlUtil.createEqIfNotNull(builder, predicates, root.get("businessId"), filter.getBusinessId());
        SqlUtil.createEqIfNotNull(builder, predicates, root.get("clientId"), filter.getClientId());
        SqlUtil.createInIfNotEmpty(predicates, root.get("statusRecord"), filter.getStatuses());

        if (CollectionUtils.isNotEmpty(predicates)) {
            query.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        TypedQuery<BaseRecordEntity> result = entityManager.createQuery(query);
        return result.getResultList();
    }

}
