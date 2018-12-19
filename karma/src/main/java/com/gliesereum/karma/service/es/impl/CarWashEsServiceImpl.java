package com.gliesereum.karma.service.es.impl;

import com.gliesereum.karma.model.document.CarWashDocument;
import com.gliesereum.karma.model.document.CarWashServiceDocument;
import com.gliesereum.karma.model.repository.es.CarWashEsRepository;
import com.gliesereum.karma.service.carwash.CarWashService;
import com.gliesereum.karma.service.common.impl.ServicePriceServiceImpl;
import com.gliesereum.karma.service.es.CarWashEsService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashDto;
import com.gliesereum.share.common.model.dto.karma.common.ServicePriceDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-18
 */

@Service
public class CarWashEsServiceImpl implements CarWashEsService {

    @Autowired
    private CarWashService carWashService;

    @Autowired
    private ServicePriceServiceImpl servicePriceService;

    @Autowired
    private DefaultConverter defaultConverter;

    @Autowired
    private CarWashEsRepository carWashEsRepository;

    @Override
    @Transactional
    @Async
    public void indexAllAsync() {

    }

    @Override
    @Transactional
    public void indexAll() {
        List<CarWashDocument> carWashDocuments = collectData();
        if (CollectionUtils.isNotEmpty(carWashDocuments)) {
            carWashEsRepository.saveAll(carWashDocuments);
        }

    }

    private List<CarWashDocument> collectData() {
        List<CarWashDocument> result = null;
        List<CarWashDto> carWashList = carWashService.getAll();
        if (CollectionUtils.isNotEmpty(carWashList)) {
            result = new ArrayList<>();
            for (CarWashDto carWash : carWashList) {
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

    private CarWashDocument insertServices(CarWashDocument target, CarWashDto source) {
        if (ObjectUtils.allNotNull(target, source)) {
            List<ServicePriceDto> servicePrices = servicePriceService.getAllByUserBusinessId(source.getId());
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

    private CarWashDocument insertGeoPoint(CarWashDocument target, CarWashDto source) {
        if (ObjectUtils.allNotNull(target, source)) {
            GeoPoint geoPoint = new GeoPoint(source.getLatitude(), source.getLongitude());
            target.setGeoPoint(geoPoint);
        }
        return target;
    }
}
