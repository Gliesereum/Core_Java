package com.gliesereum.share.common.util;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class SqlUtil {

    private SqlUtil() {}

    public static void createLikeIfNotNull(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<String> expression, String value) {
        if (value != null) {
            predicates.add(criteriaBuilder.like(expression, value));
        }
    }

    public static void createEqIfNotNull(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<?> expression, Object value) {
        if (value != null) {
            predicates.add(criteriaBuilder.equal(expression, value));
        }
    }

    public static void createInIfNotEmpty(List<Predicate> predicates, Expression<?> expression, Collection<? extends Object> value) {
        if (CollectionUtils.isNotEmpty(value)) {
            predicates.add(expression.in(value));
        }
    }

    public static void createBetweenDate(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<? extends LocalDateTime> expression, LocalDateTime from, LocalDateTime to) {
        if (ObjectUtils.allNotNull(from, to)) {
            predicates.add(criteriaBuilder.between(expression, from, to));
        } else {
            if (from != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression, from));
            }
            if (to != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(expression, to));
            }
        }
    }

    public static void createBetweenOrGreaterOrLess(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<? extends Integer> expression, Integer greater, Integer less) {
        if (ObjectUtils.allNotNull(greater, less)) {
            predicates.add(criteriaBuilder.between(expression, greater, less));
        } else {
            createGreaterThanIfNotNull(criteriaBuilder, predicates, expression, greater);
            createLessThanIfNotNull(criteriaBuilder, predicates, expression, less);
        }
    }

    public static void createBetweenOrGreaterOrLess(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<? extends Double> expression, Double greater, Double less) {
        if (ObjectUtils.allNotNull(greater, less)) {
            predicates.add(criteriaBuilder.between(expression, greater, less));
        } else if (greater != null) {
            createGreaterThanIfNotNull(criteriaBuilder, predicates, expression, greater);
        } else {
            createLessThanIfNotNull(criteriaBuilder, predicates, expression, less);
        }
    }

    public static void createGreaterThanIfNotNull(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<? extends Number> expression, Number value) {
        if (value != null) {
            predicates.add(criteriaBuilder.gt(expression, value));
        }
    }

    public static void createLessThanIfNotNull(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<? extends Number> expression, Number value) {
        if (value != null) {
            predicates.add(criteriaBuilder.lt(expression, value));
        }
    }

    public static  <E extends DefaultEntity> CriteriaQuery<Long> getCountQuery(EntityManager entityManager, List<Predicate> predicates, Class<E> entity) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<E> root = query.from(entity);
        if (CollectionUtils.isNotEmpty(predicates)) {
            query.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        if (query.isDistinct()) {
            query.select(builder.countDistinct(root));
        } else {
            query.select(builder.count(root));
        }
        return query;
    }

    public static long executeCountQuery(TypedQuery<Long> query) {
        Assert.notNull(query, "TypedQuery must not be null!");
        List<Long> totals = query.getResultList();
        long total = 0L;
        for (Long element : totals) {
            total += element == null ? 0 : element;
        }
        return total;
    }
}
