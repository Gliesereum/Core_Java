package com.gliesereum.karma.service.es.impl;

import com.gliesereum.karma.model.document.BusinessDocument;
import com.gliesereum.karma.model.document.BusinessServiceDocument;
import com.gliesereum.karma.model.repository.es.CarWashEsRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.car.CarService;
import com.gliesereum.karma.service.es.BusinessEsService;
import com.gliesereum.karma.service.service.impl.ServicePriceServiceImpl;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.BusinessSearchDto;
import com.gliesereum.share.common.model.dto.karma.car.CarInfoDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.model.dto.karma.filter.FilterAttributeDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.lucene.search.join.ScoreMode;
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
    private final static String FIELD_SERVICE_TYPE = "serviceType";

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

    @Override
    public List<BaseBusinessDto> search(BusinessSearchDto businessSearch) {
        List<BaseBusinessDto> result;
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (ObjectUtils.allNotNull(businessSearch, businessSearch.getServiceType())) {

            addQueryByServiceType(boolQueryBuilder, businessSearch.getServiceType());
            switch (businessSearch.getServiceType()) {
                case CAR_WASH: {
                    CarInfoDto carInfo = carService.getCarInfo(businessSearch.getTargetId());
                    addQueryByService(boolQueryBuilder, businessSearch.getServiceIds(), carInfo);
                }
                break;
            }

        }
        if (boolQueryBuilder.hasClauses()) {
            Iterable<BusinessDocument> searchResult = carWashEsRepository.search(boolQueryBuilder);
            List<UUID> ids = IterableUtils.toList(searchResult).stream().map(BusinessDocument::getId).map(UUID::fromString).collect(Collectors.toList());
            result = baseBusinessService.getByIds(ids);
        } else {
            result = baseBusinessService.getAll();
        }
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
    public void indexAll() {
        List<BusinessDocument> businessDocuments = collectData();
        if (CollectionUtils.isNotEmpty(businessDocuments)) {
            log.info("Run index Business to ElasticSearch");
            carWashEsRepository.saveAll(businessDocuments);
            log.info("Successful index Business to ElasticSearch");
        }
    }

    private List<BusinessDocument> collectData() {
        List<BusinessDocument> result = null;
        List<BaseBusinessDto> businessList = baseBusinessService.getAll();
        if (CollectionUtils.isNotEmpty(businessList)) {
            result = new ArrayList<>();
            for (BaseBusinessDto business : businessList) {
                BusinessDocument document = defaultConverter.convert(business, BusinessDocument.class);
                if (ObjectUtils.allNotNull(document)) {
                    document = insertGeoPoint(document, business);
                    document = insertServices(document, business);
                    result.add(document);
                }
            }
        }
        return result;
    }

    private BusinessDocument insertServices(BusinessDocument target, BaseBusinessDto source) {
        if (ObjectUtils.allNotNull(target, source)) {
            List<ServicePriceDto> servicePrices = servicePriceService.getByBusinessId(source.getId());
            if (CollectionUtils.isNotEmpty(servicePrices)) {
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
                boolQueryBuilder.must(new NestedQueryBuilder(FIELD_SERVICES, serviceRootQuery, ScoreMode.None));
            }

        }
    }

    private void addQueryByServiceType(BoolQueryBuilder boolQueryBuilder, ServiceType serviceType) {
        if (serviceType != null) {
            TermQueryBuilder serviceTypeTerm = new TermQueryBuilder(FIELD_SERVICE_TYPE, serviceType.name());
            boolQueryBuilder.must(serviceTypeTerm);
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
            query.should(new TermsQueryBuilder(firstField, firstValue));
            BoolQueryBuilder notQuery = new BoolQueryBuilder();
            notQuery.mustNot(new TermsQueryBuilder(secondField, secondValue));
            query.should(notQuery);
        }
        return query;
    }

}
