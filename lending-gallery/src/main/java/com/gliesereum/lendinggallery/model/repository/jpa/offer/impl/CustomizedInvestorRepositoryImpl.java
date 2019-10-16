package com.gliesereum.lendinggallery.model.repository.jpa.offer.impl;

import com.gliesereum.lendinggallery.model.entity.offer.InvestorOfferEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.offer.CustomizedInvestorRepository;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OfferSearchDto;
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

public class CustomizedInvestorRepositoryImpl implements CustomizedInvestorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<InvestorOfferEntity> searchInvestorOffersByParams(OfferSearchDto search) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<InvestorOfferEntity> query = builder.createQuery(InvestorOfferEntity.class);
        Root<InvestorOfferEntity> root = query.from(InvestorOfferEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        SqlUtil.createEqIfNotNull(builder, predicates, root.get("artBondId"), search.getArtBondId());
        SqlUtil.createEqIfNotNull(builder, predicates, root.get("stateType"), search.getState());
        SqlUtil.createBetweenDate(builder, predicates, root.get("create"), search.getFrom(), search.getTo());

        if (CollectionUtils.isNotEmpty(predicates)) {
            query.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        TypedQuery<InvestorOfferEntity> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
}
