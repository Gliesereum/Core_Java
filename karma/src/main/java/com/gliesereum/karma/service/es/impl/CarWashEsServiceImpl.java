package com.gliesereum.karma.service.es.impl;

import com.gliesereum.karma.model.document.CarWashDocument;
import com.gliesereum.karma.model.document.CarWashServiceDocument;
import com.gliesereum.karma.model.repository.es.CarWashEsRepository;
import com.gliesereum.karma.service.car.CarService;
import com.gliesereum.karma.service.common.BaseBusinessService;
import com.gliesereum.karma.service.common.impl.ServicePriceServiceImpl;
import com.gliesereum.karma.service.es.CarWashEsService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.car.CarInfoDto;
import com.gliesereum.share.common.model.dto.karma.common.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.common.BusinessSearchDto;
import com.gliesereum.share.common.model.dto.karma.common.ServicePriceDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
@Slf4j
public class CarWashEsServiceImpl implements CarWashEsService {

    private final static String EMPTY_FIELD_SCRIPT = "doc[''{0}''].values.length < 1";
    private final static String FIELD_SERVICES = "services";
    private final static String FIELD_SERVICE_ID = "services.serviceId";
    private final static String FIELD_CLASS_IDS = "services.serviceClassIds";
    private final static String FIELD_INTERIOR_TYPE = "services.interiorType";
    private final static String FIELD_CAR_BODY = "services.carBody";

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
        if (businessSearch != null) {
            CarInfoDto carInfo = carService.getCarInfo(businessSearch.getCarId());
            addQueryByService(boolQueryBuilder, businessSearch.getServiceIds(), carInfo);
        }
        if (boolQueryBuilder.hasClauses()) {
            Iterable<CarWashDocument> searchResult = carWashEsRepository.search(boolQueryBuilder);
            List<UUID> ids = IterableUtils.toList(searchResult).stream().map(CarWashDocument::getId).map(UUID::fromString).collect(Collectors.toList());
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
        List<CarWashDocument> carWashDocuments = collectData();
        if (CollectionUtils.isNotEmpty(carWashDocuments)) {
            log.info("Run index CarWash to ElasticSearch");
            carWashEsRepository.saveAll(carWashDocuments);
            log.info("Successful index CarWash to ElasticSearch");
        }
    }

    private List<CarWashDocument> collectData() {
        List<CarWashDocument> result = null;
        List<BaseBusinessDto> carWashList = baseBusinessService.getAll();
        if (CollectionUtils.isNotEmpty(carWashList)) {
            result = new ArrayList<>();
            for (BaseBusinessDto carWash : carWashList) {
                CarWashDocument document = defaultConverter.convert(carWash, CarWashDocument.class);
                if (ObjectUtils.allNotNull(document)) {
                    document = insertGeoPoint(document, carWash);
                    document = insertServices(document, carWash);
                    result.add(document);
                }
            }
        }
        return result;
    }

    private CarWashDocument insertServices(CarWashDocument target, BaseBusinessDto source) {
        if (ObjectUtils.allNotNull(target, source)) {
            List<ServicePriceDto> servicePrices = servicePriceService.getByBusinessId(source.getId());
            if (CollectionUtils.isNotEmpty(servicePrices)) {
                List<CarWashServiceDocument> services = servicePrices.stream()
                        .map(price -> {
                            CarWashServiceDocument serviceDocument = defaultConverter.convert(price, CarWashServiceDocument.class);
                            if (serviceDocument != null) {
                                serviceDocument.setServiceClassIds(price.getServiceClass().stream()
                                        .map(i -> i.getId().toString())
                                        .collect(Collectors.toList()));
                            }
                            return serviceDocument;
                        }).collect(Collectors.toList());
                target.setServices(services);
            }
        }
        return target;
    }

    private CarWashDocument insertGeoPoint(CarWashDocument target, BaseBusinessDto source) {
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
                    //todo check
                    /*if (carInfo.getInteriorType() != null) {
                        serviceRootQuery.must(createQueryValueExistOrEmpty(FIELD_INTERIOR_TYPE, Arrays.asList(carInfo.getInteriorType().name())));
                    }
                    if (carInfo.getCarBody() != null) {
                        serviceRootQuery.must(createQueryValueExistOrEmpty(FIELD_CAR_BODY, Arrays.asList(carInfo.getCarBody().name())));
                    }*/
                }
                boolQueryBuilder.must(new NestedQueryBuilder(FIELD_SERVICES, serviceRootQuery, ScoreMode.None));
            }

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
}
