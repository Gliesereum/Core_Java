package com.gliesereum.karma.service.es.impl;

import com.gliesereum.karma.model.document.BusinessDocument;
import com.gliesereum.karma.model.document.BusinessServiceDocument;
import com.gliesereum.karma.model.repository.es.CarWashEsRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.business.BusinessCategoryService;
import com.gliesereum.karma.service.car.CarService;
import com.gliesereum.karma.service.es.BusinessEsService;
import com.gliesereum.karma.service.preference.ClientPreferenceService;
import com.gliesereum.karma.service.service.impl.ServicePriceServiceImpl;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.base.geo.GeoDistanceDto;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.BusinessSearchDto;
import com.gliesereum.share.common.model.dto.karma.car.CarInfoDto;
import com.gliesereum.share.common.model.dto.karma.filter.FilterAttributeDto;
import com.gliesereum.share.common.model.dto.karma.preference.ClientPreferenceDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
import org.elasticsearch.script.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
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

    private final static String EMPTY_FIELD_SCRIPT = "doc[''{0}''].values.length < 1";

    private final static String FIELD_SERVICES = "services";
    private final static String FIELD_SERVICE_ID = "services.serviceId";
    private final static String FIELD_CLASS_IDS = "services.serviceClassIds";
    private final static String FIELD_FILTER_IDS = "services.filterIds";
    private final static String FIELD_FILTER_ATTRIBUTE_IDS = "services.filterAttributeIds";
    private final static String FIELD_BUSINESS_CATEGORY_ID = "businessCategoryId";
    private final static String FIELD_GEO_POINT = "geoPoint";
    private final static String FIELD_OBJECT_STATE = "objectState";

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

    @Override
    public List<BaseBusinessDto> search(BusinessSearchDto businessSearch) {
        List<BusinessDocument> businessDocuments = searchDocuments(businessSearch);
        List<UUID> ids = businessDocuments.stream().map(BusinessDocument::getId).map(UUID::fromString).collect(Collectors.toList());
        return baseBusinessService.getByIds(ids);
    }

    @Override
    public List<BusinessDocument> searchDocuments(BusinessSearchDto businessSearch) {
        List<BusinessDocument> result;
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (businessSearch != null) {
            setBusinessCategoryId(businessSearch);
            setServices(businessSearch);

            addQueryByBusinessCategoryId(boolQueryBuilder, businessSearch.getBusinessCategoryIds());
            addGeoDistanceQuery(boolQueryBuilder, businessSearch.getGeoDistance());

            CarInfoDto carInfo = carService.getCarInfo(businessSearch.getTargetId());
            addQueryByService(boolQueryBuilder, businessSearch.getServiceIds(), carInfo);

        }
        addObjectStateQuery(boolQueryBuilder, ObjectState.ACTIVE);

        Iterable<BusinessDocument> searchResult = carWashEsRepository.search(boolQueryBuilder);
        result = IterableUtils.toList(searchResult);
        return result;
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
        BaseBusinessDto business = baseBusinessService.getByIdIgnoreState(businessId);
        List<BusinessDocument> businessDocuments = collectData(Arrays.asList(business));
        index(businessDocuments);
    }


    @Override
    @Transactional
    @Async
    public void indexAsync(List<UUID> businessIds) {
        List<BaseBusinessDto> business = baseBusinessService.getByIds(businessIds);
        List<BusinessDocument> businessDocuments = collectData(business);
        index(businessDocuments);
    }

    @Override
    @Transactional
    public void indexAll() {
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
            Map<UUID, List<ServicePriceDto>> serviceMap = getServiceMap(businessList);
            for (BaseBusinessDto business : businessList) {
                BusinessDocument document = defaultConverter.convert(business, BusinessDocument.class);
                if (ObjectUtils.allNotNull(document)) {
                    document = insertGeoPoint(document, business);
                    document = insertServices(document, serviceMap.get(business.getId()));
                    if (CollectionUtils.isNotEmpty(business.getSpaces())) {
                        document.setCountBox(business.getSpaces().size());
                    }
                    result.add(document);
                }
            }
        }
        return result;
    }

    private Map<UUID, List<ServicePriceDto>> getServiceMap(List<BaseBusinessDto> business) {
        Map<UUID, List<ServicePriceDto>> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(business)) {
            List<UUID> businessIds = business.stream().map(BaseBusinessDto::getId).collect(Collectors.toList());
            result = servicePriceService.getMapByBusinessIds(businessIds);
        }
        return result;
    }

    private BusinessDocument insertServices(BusinessDocument target, List<ServicePriceDto> servicePrices) {
        if ((target != null) && CollectionUtils.isNotEmpty(servicePrices)) {
            List<BusinessServiceDocument> services = servicePrices.stream()
                    .map(price -> {
                        BusinessServiceDocument serviceDocument = defaultConverter.convert(price, BusinessServiceDocument.class);
                        if (serviceDocument != null) {
                            serviceDocument.setServiceClassIds(price.getServiceClass().stream()
                                    .map(i -> i.getId().toString())
                                    .collect(Collectors.toList()));
                            insertFilters(serviceDocument, price);
                        }
                        return serviceDocument;
                    }).collect(Collectors.toList());
            target.setServices(services);
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
                boolQueryBuilder.should(new NestedQueryBuilder(FIELD_SERVICES, serviceRootQuery, ScoreMode.Total));
            }

        }
    }

    private void addQueryByBusinessCategoryId(BoolQueryBuilder boolQueryBuilder, List<UUID> businessCategoryIds) {
        if (CollectionUtils.isNotEmpty(businessCategoryIds)) {
            List<String> businessCategoryIdString = businessCategoryIds.stream().map(UUID::toString).collect(Collectors.toList());
            TermsQueryBuilder businessCategoryTerms = new TermsQueryBuilder(FIELD_BUSINESS_CATEGORY_ID, businessCategoryIdString);
            boolQueryBuilder.must(businessCategoryTerms);
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
        if (geoDistance != null) {
            if (ObjectUtils.allNotNull(geoDistance.getLatitude(), geoDistance.getLongitude(), geoDistance.getDistanceMeters())) {
                GeoDistanceQueryBuilder geoDistanceQueryBuilder = new GeoDistanceQueryBuilder(FIELD_GEO_POINT)
                        .point(geoDistance.getLatitude(), geoDistance.getLongitude())
                        .distance(geoDistance.getDistanceMeters(), DistanceUnit.METERS);
                boolQueryBuilder.filter(geoDistanceQueryBuilder);
            }
        }
    }

    private void addObjectStateQuery(BoolQueryBuilder boolQueryBuilder, ObjectState objectState) {
        if (objectState != null) {
            boolQueryBuilder.must(new TermQueryBuilder(FIELD_OBJECT_STATE, objectState.toString()));
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
        if (businessSearch != null) {
            if (CollectionUtils.isEmpty(businessSearch.getServiceIds())) {
                if (!SecurityUtil.isAnonymous()) {
                    List<ClientPreferenceDto> clientPreference = null;
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
        }
    }
}
