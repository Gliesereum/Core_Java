package com.gliesereum.karma.model.repository.jpa.business.impl;

import com.gliesereum.karma.model.entity.business.WorkerEntity;
import com.gliesereum.karma.model.repository.jpa.business.WorkerLockableRepository;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class WorkerLockableRepositoryImpl implements WorkerLockableRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<WorkerEntity> findByWorkingSpaceIdWithLock(UUID workingSpaceId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<WorkerEntity> query = builder.createQuery(WorkerEntity.class);
        Root<WorkerEntity> root = query.from(WorkerEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        createEqIfNotNull(builder, predicates, root.get("workingSpaceId"), workingSpaceId);

        if (CollectionUtils.isNotEmpty(predicates)) {
            query.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        TypedQuery<WorkerEntity> typedQuery = entityManager.createQuery(query);
        typedQuery.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        return typedQuery.getResultList();
    }

    private void createEqIfNotNull(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<?> expression, Object value) {
        if (value != null) {
            predicates.add(criteriaBuilder.equal(expression, value));
        }
    }
}
