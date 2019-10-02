package com.gliesereum.karma.model.repository.jpa.record.impl;

import com.gliesereum.karma.model.entity.record.BaseRecordEntity;
import com.gliesereum.karma.model.entity.record.RecordServiceEntity;
import com.gliesereum.karma.model.repository.jpa.record.BaseRecordSearchRepository;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusProcess;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.record.RecordPaymentInfoDto;
import com.gliesereum.share.common.model.dto.karma.record.RecordUsageCountDto;
import com.gliesereum.share.common.model.dto.karma.record.search.BusinessRecordSearchDto;
import com.gliesereum.share.common.model.dto.karma.record.search.BusinessRecordSearchPageableDto;
import com.gliesereum.share.common.util.SqlUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.PageableExecutionUtils;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class BaseRecordSearchRepositoryImpl implements BaseRecordSearchRepository {

    private static final String GET_BY_TIME_BETWEEN_QUERY =
            "SELECT r.* " +
                    "FROM karma.record as r " +
                    "LEFT JOIN karma.business as b on r.business_id=b.id " +
                    "WHERE (r.begin between (to_timestamp(:from, 'YYYY-MM-DD HH24:MI:SS') + ((b.time_zone + :minutesFrom) * interval '1 minute')) AND (to_timestamp(:from, 'YYYY-MM-DD HH24:MI:SS') + ((b.time_zone + :minutesTo) * interval '1 minute'))) AND " +
                    "r.status_record = :status AND r.notification_send = :notificationSend";

    private static final String GET_BY_FINISH_TIME_IN_PAST_QUERY =
            "SELECT r.* " +
                    "FROM karma.record as r " +
                    "LEFT JOIN karma.business as b on r.business_id=b.id " +
                    "WHERE r.finish <=  (to_timestamp(:from, 'YYYY-MM-DD HH24:MI:SS') + ((b.time_zone) * interval '1 minute')) AND " +
                    "r.status_process = :status";


    private static final String COUNT_DISTINCT_WORKER_ID_BY_TIME_BETWEEN_QUERY =
            "SELECT COUNT(DISTINCT r.worker_id) " +
                    "FROM karma.record as r " +
                    "LEFT JOIN karma.business as b on r.business_id=b.id " +
                    "WHERE ((to_timestamp(:time, 'YYYY-MM-DD HH24:MI:SS') + ((b.time_zone) * interval '1 minute')) >= r.begin) AND " +
                    "((to_timestamp(:time, 'YYYY-MM-DD HH24:MI:SS') + ((b.time_zone) * interval '1 minute')) <= r.finish) AND " +
                    "r.status_record = :status";
    
    private static final String COUNT_PACKAGE_USAGE =
            "SELECT business_id, package_id, count(package_id) " +
                    "FROM karma.record " +
                    "WHERE begin >= (to_timestamp(:from, 'YYYY-MM-DD HH24:MI:SS')) AND business_id in (:businessIds) AND package_id notnull " +
                    "GROUP BY business_id, package_id " +
                    "ORDER BY count(package_id) DESC " +
                    "LIMIT :limit";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<BaseRecordEntity> getByTimeBetween(LocalDateTime from, Integer minutesFrom, Integer minutesTo, StatusRecord status, boolean notificationSend) {
        List<BaseRecordEntity> result = null;
        if (ObjectUtils.allNotNull(from, minutesFrom, minutesTo, status, notificationSend)) {
            Query query = entityManager.createNativeQuery(GET_BY_TIME_BETWEEN_QUERY, BaseRecordEntity.class);
            query.setParameter("from", from.toString());
            query.setParameter("minutesFrom", minutesFrom);
            query.setParameter("minutesTo", minutesTo);
            query.setParameter("status", status.name());
            query.setParameter("notificationSend", notificationSend);
            result = query.getResultList();
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<BaseRecordEntity> getByFinishTimeInPast(LocalDateTime from, StatusProcess status) {
        List<BaseRecordEntity> result = null;
        if (ObjectUtils.allNotNull(from, status)) {
            Query query = entityManager.createNativeQuery(GET_BY_FINISH_TIME_IN_PAST_QUERY, BaseRecordEntity.class);
            query.setParameter("from", from.toString());
            query.setParameter("status", status.name());
            result = query.getResultList();
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public long countBusyWorker(LocalDateTime time, StatusRecord status) {
        long result = 0L;
        if (ObjectUtils.allNotNull(time, status)) {
            Query query = entityManager.createNativeQuery(COUNT_DISTINCT_WORKER_ID_BY_TIME_BETWEEN_QUERY);
            query.setParameter("time", time.toString());
            query.setParameter("status", status.name());
            List<BigInteger> resultList = query.getResultList();
            for (BigInteger element : resultList) {
                result += element == null ? 0 : element.longValue();
            }
        }
        return result;
    }

    @Override
    public Page<BaseRecordEntity> getRecordsBySearchDto(BusinessRecordSearchPageableDto search, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BaseRecordEntity> query = builder.createQuery(BaseRecordEntity.class);
        Root<BaseRecordEntity> root = query.from(BaseRecordEntity.class);

        List<Predicate> predicates = getPredicateForSearch(root, builder, search);

        if (CollectionUtils.isNotEmpty(predicates)) {
            query.where(predicates.toArray(new Predicate[predicates.size()]));
        }

        query.orderBy(toOrders(pageable.getSortOr(Sort.by(Sort.Direction.DESC, "begin")), root, builder));

        TypedQuery<BaseRecordEntity> typedQuery = entityManager.createQuery(query);

        if (pageable.isPaged()) {
            typedQuery.setFirstResult((int) pageable.getOffset());
            typedQuery.setMaxResults(pageable.getPageSize());
        }

        return PageableExecutionUtils.getPage(typedQuery.getResultList(), pageable,
                () -> SqlUtil.executeCountQuery(entityManager.createQuery(SqlUtil.getCountQuery(entityManager, predicates, BaseRecordEntity.class))));
    }

    @Override
    public RecordPaymentInfoDto getPaymentInfoBySearch(BusinessRecordSearchDto search) {
        long result = 0L;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> query = builder.createQuery(Integer.class);
        Root<BaseRecordEntity> root = query.from(BaseRecordEntity.class);

        List<Predicate> predicates = getPredicateForSearch(root, builder, search);
        if (CollectionUtils.isNotEmpty(predicates)) {
            query.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        query.select(builder.sum(root.get("price")));

        TypedQuery<Integer> typedQuery = entityManager.createQuery(query);
        List<Integer> resultList = typedQuery.getResultList();
        if (CollectionUtils.isNotEmpty(resultList)) {
            for (Integer element : resultList) {
                result += element == null ? 0 : element;
            }
        }

        RecordPaymentInfoDto recordPaymentInfoDto = new RecordPaymentInfoDto();
        recordPaymentInfoDto.setSum(result);
        return recordPaymentInfoDto;
    }

    
    @Override
    public List<RecordUsageCountDto> getCountPackageUsage(LocalDateTime beginFrom, Collection<UUID> businessIds, Long limit) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
        Root<BaseRecordEntity> root = query.from(BaseRecordEntity.class);
    
        List<Predicate> predicates = new ArrayList<>();
        
        predicates.add(builder.isNotNull(root.get("packageId")));
        predicates.add(builder.greaterThan(root.get("begin"), beginFrom));
        SqlUtil.createInIfNotEmpty(predicates, root.get("businessId"), businessIds);
        
        query.where(predicates.toArray(new Predicate[predicates.size()]));
        query.groupBy(root.get("businessId"), root.get("packageId"));
        query.orderBy(builder.desc(builder.count(root.get("packageId"))));
        query.multiselect(root.get("businessId"), root.get("packageId"), builder.count(root.get("packageId")));
    
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(limit.intValue());
        List<Object[]> resultList = typedQuery.getResultList();
        List<RecordUsageCountDto> result = resultList.stream().map(i -> {
            RecordUsageCountDto recordUsageCountDto = new RecordUsageCountDto();
            recordUsageCountDto.setBusinessId((UUID)i[0]);
            recordUsageCountDto.setObjectId((UUID)i[1]);
            recordUsageCountDto.setCount((Long)i[2]);
            return recordUsageCountDto;
        }).collect(Collectors.toList());
       return result;
    }
    
    @Override
    public List<RecordUsageCountDto> getCountServiceUsage(LocalDateTime beginFrom, Collection<UUID> businessIds, Long limit) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
        Root<BaseRecordEntity> root = query.from(BaseRecordEntity.class);
        Join<Object, Object> join = root.join("services", JoinType.LEFT);
    
        List<Predicate> predicates = new ArrayList<>();
    
        predicates.add(builder.isNotNull(join.get("serviceId")));
        predicates.add(builder.greaterThan(root.get("begin"), beginFrom));
        SqlUtil.createInIfNotEmpty(predicates, root.get("businessId"), businessIds);
    
        query.where(predicates.toArray(new Predicate[predicates.size()]));
        query.groupBy(root.get("businessId"), join.get("serviceId"));
        query.orderBy(builder.desc(builder.count(join.get("serviceId"))));
        query.multiselect(root.get("businessId"), join.get("serviceId"), builder.count(join.get("serviceId")));
    
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(limit.intValue());
        List<Object[]> resultList = typedQuery.getResultList();
        List<RecordUsageCountDto> result = resultList.stream().map(i -> {
            RecordUsageCountDto recordUsageCountDto = new RecordUsageCountDto();
            recordUsageCountDto.setBusinessId((UUID)i[0]);
            recordUsageCountDto.setObjectId((UUID)i[1]);
            recordUsageCountDto.setCount((Long)i[2]);
            return recordUsageCountDto;
        }).collect(Collectors.toList());
        return result;
    }
    
    @Override
    public List<RecordUsageCountDto> getCountWorkerUsage(LocalDateTime beginFrom, Collection<UUID> businessIds, Long limit) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
        Root<BaseRecordEntity> root = query.from(BaseRecordEntity.class);
    
        List<Predicate> predicates = new ArrayList<>();
    
        predicates.add(builder.isNotNull(root.get("workerId")));
        predicates.add(builder.greaterThan(root.get("begin"), beginFrom));
        SqlUtil.createInIfNotEmpty(predicates, root.get("businessId"), businessIds);
    
        query.where(predicates.toArray(new Predicate[predicates.size()]));
        query.groupBy(root.get("businessId"), root.get("workerId"));
        query.orderBy(builder.desc(builder.count(root.get("workerId"))));
        query.multiselect(root.get("businessId"), root.get("workerId"), builder.count(root.get("workerId")));
    
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(limit.intValue());
        List<Object[]> resultList = typedQuery.getResultList();
        List<RecordUsageCountDto> result = resultList.stream().map(i -> {
            RecordUsageCountDto recordUsageCountDto = new RecordUsageCountDto();
            recordUsageCountDto.setBusinessId((UUID)i[0]);
            recordUsageCountDto.setObjectId((UUID)i[1]);
            recordUsageCountDto.setCount((Long)i[2]);
            return recordUsageCountDto;
        }).collect(Collectors.toList());
        return result;
    }
    
    @Override
    public List<RecordUsageCountDto> getCountRecordInBusiness(LocalDateTime beginFrom, Collection<UUID> businessIds, Long limit) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
        Root<BaseRecordEntity> root = query.from(BaseRecordEntity.class);
    
        List<Predicate> predicates = new ArrayList<>();
        
        predicates.add(builder.greaterThan(root.get("begin"), beginFrom));
        SqlUtil.createInIfNotEmpty(predicates, root.get("businessId"), businessIds);
    
        query.where(predicates.toArray(new Predicate[predicates.size()]));
        query.groupBy(root.get("businessId"));
        query.orderBy(builder.desc(builder.count(root.get("id"))));
        query.multiselect(root.get("businessId"), builder.count(root.get("id")));
    
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(query);
        typedQuery.setMaxResults(limit.intValue());
        List<Object[]> resultList = typedQuery.getResultList();
        List<RecordUsageCountDto> result = resultList.stream().map(i -> {
            RecordUsageCountDto recordUsageCountDto = new RecordUsageCountDto();
            recordUsageCountDto.setBusinessId((UUID)i[0]);
            recordUsageCountDto.setCount((Long)i[1]);
            return recordUsageCountDto;
        }).collect(Collectors.toList());
        return result;
    }
    
    private List<Predicate> getPredicateForSearch(Root<BaseRecordEntity> root, CriteriaBuilder builder, BusinessRecordSearchDto search) {
        List<Predicate> predicates = new ArrayList<>();
        if (search != null) {
            SqlUtil.createEqIfNotNull(builder, predicates, root.get("recordNumber"), search.getRecordNumber());
            SqlUtil.createBetweenDate(builder, predicates, root.get("begin"), search.getFrom(), search.getTo());
            SqlUtil.createInIfNotEmpty(predicates, root.get("statusRecord"), search.getStatus());
            SqlUtil.createInIfNotEmpty(predicates, root.get("statusProcess"), search.getProcesses());
            SqlUtil.createInIfNotEmpty(predicates, root.get("businessId"), search.getBusinessIds());
            SqlUtil.createInIfNotEmpty(predicates, root.get("clientId"), search.getClientIds());
            SqlUtil.createInIfNotEmpty(predicates, root.get("workingSpaceId"), search.getWorkingSpaceIds());
        }
        return predicates;
    }

}
