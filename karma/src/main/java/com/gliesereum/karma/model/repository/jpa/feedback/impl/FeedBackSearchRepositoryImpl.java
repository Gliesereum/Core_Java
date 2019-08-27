package com.gliesereum.karma.model.repository.jpa.feedback.impl;

import com.gliesereum.karma.model.entity.feedback.FeedBackUserEntity;
import com.gliesereum.karma.model.repository.jpa.feedback.FeedBackSearchRepository;
import com.gliesereum.share.common.model.dto.karma.feedback.FeedBackSearchDto;
import com.gliesereum.share.common.util.SqlUtil;

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
public class FeedBackSearchRepositoryImpl implements FeedBackSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FeedBackUserEntity> getBySearch(FeedBackSearchDto search) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FeedBackUserEntity> query = builder.createQuery(FeedBackUserEntity.class);
        Root<FeedBackUserEntity> root = query.from(FeedBackUserEntity.class);
        List<Predicate> predicates = new ArrayList<>();
        SqlUtil.createBetweenDate(builder, predicates, root.get("dateCreateObject"), search.getDateCreateObjectFrom(), search.getDateCreateObjectTo());
        SqlUtil.createBetweenDate(builder, predicates, root.get("dateFeedback"), search.getDateFeedBackFrom(), search.getDateFeedBackTo());
        SqlUtil.createEqIfNotNull(builder, predicates, root.get("objectId"), search.getObjectId());
        SqlUtil.createEqIfNotNull(builder, predicates, root.get("workingSpaceId"), search.getWorkingSpaceId());
        SqlUtil.createEqIfNotNull(builder, predicates, root.get("workerId"), search.getWorkerId());
        SqlUtil.createInIfNotEmpty(predicates, root.get("businessId"), search.getBusinessIds());
        SqlUtil.createBetweenOrGreaterOrLess(builder, predicates, root.get("mark"), search.getMarkFrom(), search.getMarkTo());
        TypedQuery<FeedBackUserEntity> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

}
