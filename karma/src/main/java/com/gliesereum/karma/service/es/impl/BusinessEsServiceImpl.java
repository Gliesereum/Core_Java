package com.gliesereum.karma.service.es.impl;

import com.gliesereum.karma.model.document.BusinessDocument;
import com.gliesereum.karma.model.document.BusinessServiceDocument;
import com.gliesereum.karma.model.repository.es.CarWashEsRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.business.BusinessCategoryService;
import com.gliesereum.karma.service.car.CarService;
import com.gliesereum.karma.service.comment.CommentService;
import com.gliesereum.karma.service.es.BusinessEsService;
import com.gliesereum.karma.service.preference.ClientPreferenceService;
import com.gliesereum.karma.service.service.impl.ServicePriceServiceImpl;
import com.gliesereum.karma.service.tag.BusinessTagService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.base.geo.GeoDistanceDto;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.BusinessSearchDto;
import com.gliesereum.share.common.model.dto.karma.car.CarInfoDto;
import com.gliesereum.share.common.model.dto.karma.comment.RatingDto;
import com.gliesereum.share.common.model.dto.karma.filter.FilterAttributeDto;
import com.gliesereum.share.common.model.dto.karma.preference.ClientPreferenceDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
import com.gliesereum.share.common.model.dto.karma.tag.TagDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
import org.elasticsearch.script.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
@Slf4j
public class BusinessEsServiceImpl implements BusinessEsService {

    private static final String EMPTY_FIELD_SCRIPT = "doc[''{0}''].values.length < 1";

    private static final String FIELD_SERVICES = "services";
    private static final String FIELD_SERVICE_ID = "services.serviceId";
    private static final String FIELD_CLASS_IDS = "services.serviceClassIds";
    private static final String FIELD_FILTER_IDS = "services.filterIds";
    private static final String FIELD_FILTER_ATTRIBUTE_IDS = "services.filterAttributeIds";
    private static final String FIELD_BUSINESS_CATEGORY_ID = "businessCategoryId";
    private static final String FIELD_CORPORATION_ID = "corporationId";
    private static final String FIELD_GEO_POINT = "geoPoint";
    private static final String FIELD_OBJECT_STATE = "objectState";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_ADDRESS = "address";
    private static final String FIELD_SERVICE_NAMES = "serviceNames";
    private static final String FIELD_BUSINESS_VERIFY = "businessVerify";
    private static final String FIELD_TAG = "tags";
    private static final String FIELD_WORK_TIMES = "workTimes";
    
    public static final String BUSINESS_INDEX_NAME = "karma-business";
    public static final String BUSINESS_TYPE_NAME = "business";

    @Autowired
    private BaseBusinessService baseBusinessService;

    @Autowired
    private CarService carService;

    @Autowired
    private ServicePriceServiceImpl servicePriceService;

    @Autowired
    private DefaultConverter defaultConverter;

    @Autowired
    private CarWashEsRepository carWashEsRepository;

    @Autowired
    private BusinessCategoryService businessCategoryService;

    @Autowired
    private ClientPreferenceService clientPreferenceService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BusinessTagService businessTagService;
    
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<BaseBusinessDto> search(BusinessSearchDto businessSearch) {
        List<BusinessDocument> businessDocuments = searchDocuments(businessSearch);
        List<UUID> ids = businessDocuments.stream().map(BusinessDocument::getId).map(UUID::fromString).collect(Collectors.toList());
        return baseBusinessService.getByIds(ids);
    }

    @Override
    public List<BusinessDocument> searchDocuments(BusinessSearchDto businessSearch) {
        List<BusinessDocument> result;
    
        NativeSearchQueryBuilder nativeSearchQueryBuilder = buildQuery(businessSearch);
    
        Iterable<BusinessDocument> searchResult = carWashEsRepository.search(nativeSearchQueryBuilder.build());
        //Iterable<BusinessDocument> searchResult = carWashEsRepository.search(nativeQuery.getQuery());
        result = IterableUtils.toList(searchResult);
        return result;
    }
    
    @Override
    public Page<BusinessDocument> searchDocumentsPage(BusinessSearchDto businessSearch) {
        Page<BusinessDocument> result;
    
        NativeSearchQueryBuilder nativeSearchQueryBuilder = buildQuery(businessSearch);
        setPageable(nativeSearchQueryBuilder, businessSearch);
    
        result = carWashEsRepository.search(nativeSearchQueryBuilder.build());
        return result;
    }
    
    
    private NativeSearchQueryBuilder buildQuery(BusinessSearchDto businessSearch) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (businessSearch != null) {
            setBusinessCategoryId(businessSearch);
            setServices(businessSearch);
            addQueryByCorporationId(boolQueryBuilder, businessSearch.getCorporationIds());
            addQueryByBusinessCategoryId(boolQueryBuilder, businessSearch.getBusinessCategoryIds());
            addGeoDistanceQuery(boolQueryBuilder, businessSearch.getGeoDistance());
            addFullTextQuery(boolQueryBuilder, businessSearch.getFullTextQuery());
            addBusinessVerifyStateQuery(boolQueryBuilder, businessSearch.getBusinessVerify());
            addQueryByTags(boolQueryBuilder, businessSearch.getTags());
        
            CarInfoDto carInfo = null;
            if (!SecurityUtil.isAnonymous()) {
                carInfo = carService.getCarInfo(businessSearch.getTargetId());
            }
            addQueryByService(boolQueryBuilder, businessSearch.getServiceIds(), carInfo);
        
        }
        addObjectStateQuery(boolQueryBuilder, ObjectState.ACTIVE);
    
        FetchSourceFilter fetchSourceFilter = new FetchSourceFilter(null, new String[]{FIELD_SERVICE_NAMES, FIELD_SERVICES, FIELD_WORK_TIMES});
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).withSourceFilter(fetchSourceFilter).withIndices(BUSINESS_INDEX_NAME).withTypes(BUSINESS_TYPE_NAME);
        setPageable(nativeSearchQueryBuilder, businessSearch);
        return nativeSearchQueryBuilder;
    }
    
    @Override
    @Transactional
    @Async
    public void indexAllAsync() {
        indexAll();
    }

    @Override
    @Transactional
    @Async
    public void indexAsync(UUID businessId) {
        log.info("Get data for index Business to ElasticSearch");
        BaseBusinessDto business = baseBusinessService.getByIdIgnoreState(businessId);
        List<BusinessDocument> businessDocuments = collectData(Arrays.asList(business));
        index(businessDocuments);
    }


    @Override
    @Transactional
    @Async
    public void indexAsync(List<UUID> businessIds) {
        log.info("Get data for index Business to ElasticSearch");
        List<BaseBusinessDto> business = baseBusinessService.getByIds(businessIds);
        List<BusinessDocument> businessDocuments = collectData(business);
        index(businessDocuments);
    }

    @Override
    @Transactional
    public void indexAll() {
        log.info("Get data for index Business to ElasticSearch");
        List<BaseBusinessDto> businessList = baseBusinessService.getAllIgnoreState();
        List<BusinessDocument> businessDocuments = collectData(businessList);
        index(businessDocuments);
    }

    private void index(List<BusinessDocument> businessDocuments) {
        if (CollectionUtils.isNotEmpty(businessDocuments)) {
            log.info("Run index Business to ElasticSearch");
            carWashEsRepository.saveAll(businessDocuments);
            log.info("Successful index Business to ElasticSearch");
        }
    }

    private List<BusinessDocument> collectData(List<BaseBusinessDto> businessList) {
        List<BusinessDocument> result = null;
        if (CollectionUtils.isNotEmpty(businessList)) {
            result = new ArrayList<>();
            List<UUID> businessIds = businessList.stream().map(BaseBusinessDto::getId).collect(Collectors.toList());
            Map<UUID, RatingDto> ratingMap = commentService.getRatings(businessIds);
            Map<UUID, List<ServicePriceDto>> serviceMap = getServiceMap(businessIds);
            Map<UUID, List<TagDto>> tagMap = businessTagService.getMapByBusinessIds(businessIds);
            for (BaseBusinessDto business : businessList) {
                BusinessDocument document = defaultConverter.convert(business, BusinessDocument.class);
                if (document != null) {
                    insertGeoPoint(document, business);
                    insertServices(document, serviceMap.get(business.getId()));
                    insertRating(document, ratingMap.get(business.getId()));
                    insertTags(document, tagMap.get(business.getId()));
                    if (CollectionUtils.isNotEmpty(business.getSpaces())) {
                        document.setCountBox(business.getSpaces().size());
                    }
                    result.add(document);
                }
            }
        }
        return result;
    }

    private Map<UUID, List<ServicePriceDto>> getServiceMap(List<UUID> businessIds) {
        Map<UUID, List<ServicePriceDto>> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(businessIds)) {
            result = servicePriceService.getMapByBusinessIds(businessIds);
        }
        return result;
    }

    private void insertRating(BusinessDocument target, RatingDto rating) {
        if (ObjectUtils.allNotNull(target, rating)) {
            target.setRating(rating.getRating().doubleValue());
            target.setRatingCount(rating.getCount());
        }
    }

    private BusinessDocument insertServices(BusinessDocument target, List<ServicePriceDto> servicePrices) {
        if ((target != null) && CollectionUtils.isNotEmpty(servicePrices)) {
            Set<String> serviceNames = new HashSet<>();
            List<BusinessServiceDocument> services = servicePrices.stream()
                    .map(price -> {
                        BusinessServiceDocument serviceDocument = defaultConverter.convert(price, BusinessServiceDocument.class);
                        if (serviceDocument != null) {
                            serviceDocument.setServiceClassIds(price.getServiceClass().stream()
                                    .map(i -> i.getId().toString())
                                    .collect(Collectors.toList()));
                            insertFilters(serviceDocument, price);
                            serviceNames.add(price.getName());
                            serviceNames.add(price.getService().getName());
                        }
                        return serviceDocument;
                    }).collect(Collectors.toList());
            target.setServices(services);
            target.setServiceNames(new ArrayList<>(serviceNames));
        }
        return target;
    }

    private BusinessServiceDocument insertFilters(BusinessServiceDocument target, ServicePriceDto source) {
        if (ObjectUtils.allNotNull(target, source) && CollectionUtils.isNotEmpty(source.getAttributes())) {
            Set<String> filterIds = new HashSet<>();
            Set<String> filterAttributeIds = new HashSet<>();
            for (FilterAttributeDto attribute : source.getAttributes()) {
                filterIds.add(attribute.getFilterId().toString());
                filterAttributeIds.add(attribute.getId().toString());
            }
            target.setFilterIds(new ArrayList<>(filterIds));
            target.setFilterAttributeIds(new ArrayList<>(filterAttributeIds));
        }
        return target;
    }

    private BusinessDocument insertGeoPoint(BusinessDocument target, BaseBusinessDto source) {
        if (ObjectUtils.allNotNull(target, source)) {
            GeoPoint geoPoint = new GeoPoint(source.getLatitude(), source.getLongitude());
            target.setGeoPoint(geoPoint);
        }
        return target;
    }

    private BusinessDocument insertTags(BusinessDocument target, List<TagDto> tags) {
        if ((target != null) && CollectionUtils.isNotEmpty(tags)) {
            Set<String> tagNames = new HashSet<>();
            tagNames = tags.stream().map(TagDto::getName).collect(Collectors.toSet());
            target.setTags(new ArrayList<>(tagNames));
        }
        return target;
    }

    private void addQueryByService(BoolQueryBuilder boolQueryBuilder, List<UUID> serviceIds, CarInfoDto carInfo) {
        if (CollectionUtils.isNotEmpty(serviceIds)) {
            for (UUID serviceId : serviceIds) {
                BoolQueryBuilder serviceRootQuery = new BoolQueryBuilder();
                serviceRootQuery.must(new TermQueryBuilder(FIELD_SERVICE_ID, serviceId.toString()));
                if (carInfo != null) {
                    serviceRootQuery.must(createQueryValueExistOrEmpty(FIELD_CLASS_IDS, carInfo.getServiceClassIds()));
                    if (CollectionUtils.isNotEmpty(carInfo.getFilterAttributes())) {
                        for (FilterAttributeDto filterAttribute : carInfo.getFilterAttributes()) {
                            serviceRootQuery.must(createQueryValueExistOrNotExitInSecond(FIELD_FILTER_ATTRIBUTE_IDS, FIELD_FILTER_IDS, filterAttribute.getId().toString(), filterAttribute.getFilterId().toString()));
                        }
                    }
                }
                boolQueryBuilder.must(new NestedQueryBuilder(FIELD_SERVICES, serviceRootQuery, ScoreMode.Total));
            }

        }
    }

    private void addQueryByBusinessCategoryId(BoolQueryBuilder boolQueryBuilder, List<UUID> businessCategoryIds) {
        if (CollectionUtils.isNotEmpty(businessCategoryIds)) {
            List<String> businessCategoryIdString = businessCategoryIds.stream().filter(Objects::nonNull).map(UUID::toString).collect(Collectors.toList());
            TermsQueryBuilder businessCategoryTerms = new TermsQueryBuilder(FIELD_BUSINESS_CATEGORY_ID, businessCategoryIdString);
            boolQueryBuilder.must(businessCategoryTerms);
        }
    }

    private void addQueryByCorporationId(BoolQueryBuilder boolQueryBuilder, List<UUID> corporationIds) {
        if (CollectionUtils.isNotEmpty(corporationIds)) {
            List<String> corporationIdsString = corporationIds.stream().filter(Objects::nonNull).map(UUID::toString).collect(Collectors.toList());
            TermsQueryBuilder corporationTerms = new TermsQueryBuilder(FIELD_CORPORATION_ID, corporationIdsString);
            boolQueryBuilder.must(corporationTerms);
        }
    }

    private void addQueryByTags(BoolQueryBuilder boolQueryBuilder, List<String> tags) {
        if (CollectionUtils.isNotEmpty(tags)) {
            boolQueryBuilder.must(QueryBuilders.termsQuery(FIELD_TAG, tags));
        }
    }

    private BoolQueryBuilder createQueryValueExistOrEmpty(String fieldName, List<String> values) {
        BoolQueryBuilder query = new BoolQueryBuilder();
        if (CollectionUtils.isNotEmpty(values)) {
            query.should(new TermsQueryBuilder(fieldName, values));
            String script = MessageFormat.format(EMPTY_FIELD_SCRIPT, fieldName);
            query.should(new ScriptQueryBuilder(new Script(script)));
        }
        return query;
    }

    private BoolQueryBuilder createQueryValueExistOrNotExitInSecond(String firstField, String secondField, String firstValue, String secondValue) {
        BoolQueryBuilder query = new BoolQueryBuilder();
        if (ObjectUtils.allNotNull(firstValue, secondValue)) {
            query.should(new TermsQueryBuilder(firstField, firstValue).boost(10f));
            BoolQueryBuilder notQuery = new BoolQueryBuilder();
            notQuery.mustNot(new TermsQueryBuilder(secondField, secondValue));
            query.should(notQuery);
        }
        return query;
    }

    private void addGeoDistanceQuery(BoolQueryBuilder boolQueryBuilder, GeoDistanceDto geoDistance) {
        if ((geoDistance != null) && ObjectUtils.allNotNull(geoDistance.getLatitude(), geoDistance.getLongitude(), geoDistance.getDistanceMeters())) {
            GeoDistanceQueryBuilder geoDistanceQueryBuilder = new GeoDistanceQueryBuilder(FIELD_GEO_POINT)
                    .point(geoDistance.getLatitude(), geoDistance.getLongitude())
                    .distance(geoDistance.getDistanceMeters(), DistanceUnit.METERS);
            boolQueryBuilder.filter(geoDistanceQueryBuilder);
        }
    }

    private void addFullTextQuery(BoolQueryBuilder boolQueryBuilder, String fullTextQuery) {
        if (StringUtils.isNoneBlank(fullTextQuery) && fullTextQuery.length() > 2) {

            Map<String, Float> fields = Map.of(
                    FIELD_NAME, 2.0F,
                    FIELD_DESCRIPTION, 2.0F,
                    FIELD_ADDRESS, 2.0F,
                    FIELD_SERVICE_NAMES, 2.0F);
            boolQueryBuilder.must(QueryBuilders.queryStringQuery(fullTextQuery).fields(fields).type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX));
        }
    }

    private void addObjectStateQuery(BoolQueryBuilder boolQueryBuilder, ObjectState objectState) {
        if (objectState != null) {
            boolQueryBuilder.must(new TermQueryBuilder(FIELD_OBJECT_STATE, objectState.toString()));
        }
    }

    private void addBusinessVerifyStateQuery(BoolQueryBuilder boolQueryBuilder, Boolean businessVerify) {
        if (businessVerify != null) {
            boolQueryBuilder.must(new TermQueryBuilder(FIELD_BUSINESS_VERIFY, businessVerify));
        }
    }

    private void setBusinessCategoryId(BusinessSearchDto businessSearch) {
        if (businessSearch != null) {
            if (CollectionUtils.isEmpty(businessSearch.getBusinessCategoryIds())) {
                businessSearch.setBusinessCategoryIds(new ArrayList<>());
            }
            UUID businessCategoryId = businessSearch.getBusinessCategoryId();
            if (businessCategoryId != null) {
                businessSearch.getBusinessCategoryIds().add(businessCategoryId);
            }
        }
    }

    private void setServices(BusinessSearchDto businessSearch) {
        if ((businessSearch != null) && CollectionUtils.isEmpty(businessSearch.getServiceIds()) && !SecurityUtil.isAnonymous()) {
            List<ClientPreferenceDto> clientPreference;
            UUID userId = SecurityUtil.getUserId();
            List<UUID> businessCategoryIds = businessSearch.getBusinessCategoryIds();
            if (CollectionUtils.isEmpty(businessCategoryIds)) {
                clientPreference = clientPreferenceService.getAllByUserId(userId);
            } else {
                clientPreference = clientPreferenceService.getAllByUserIdAndBusinessCategoryIds(userId, businessCategoryIds);
            }
            if (CollectionUtils.isNotEmpty(clientPreference)) {
                businessSearch.setServiceIds(clientPreference.stream().map(ClientPreferenceDto::getServiceId).collect(Collectors.toList()));
            }

        }
    }
    
    private void setPageable(NativeSearchQueryBuilder nativeSearchQueryBuilder, BusinessSearchDto businessSearchDto) {
        int page = 1;
        if ((businessSearchDto != null) && (businessSearchDto.getPage() != null) && (businessSearchDto.getPage() > 0)) {
            page = businessSearchDto.getPage();
        }
        if ((businessSearchDto != null) && (businessSearchDto.getSize() != null) && (businessSearchDto.getSize() > 0)) {
            nativeSearchQueryBuilder.withPageable(PageRequest.of(page, businessSearchDto.getSize()));
        } else {
            long count = elasticsearchOperations.count(nativeSearchQueryBuilder.build(), carWashEsRepository.getEntityClass());
            nativeSearchQueryBuilder.withPageable(PageRequest.of(page, (int)count));
        }
    }
}
