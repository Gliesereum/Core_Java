package com.gliesereum.karma.service.business.impl;

import com.gliesereum.karma.facade.client.ClientFacade;
import com.gliesereum.karma.model.entity.business.BaseBusinessEntity;
import com.gliesereum.karma.model.repository.jpa.business.BaseBusinessRepository;
import com.gliesereum.karma.service.business.*;
import com.gliesereum.karma.service.car.CarService;
import com.gliesereum.karma.service.es.ClientEsService;
import com.gliesereum.karma.service.media.MediaService;
import com.gliesereum.karma.service.record.BaseRecordService;
import com.gliesereum.karma.service.service.PackageService;
import com.gliesereum.karma.service.service.ServicePriceService;
import com.gliesereum.karma.service.service.ServiceService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.karma.business.*;
import com.gliesereum.share.common.model.dto.karma.car.CarDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.*;
import com.gliesereum.share.common.model.dto.karma.media.MediaDto;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.model.dto.karma.service.PackageDto;
import com.gliesereum.share.common.model.dto.karma.service.ServiceDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author vitalij
 * @version 1.0
 */
@Service
@Slf4j
public class DataLoadingServiceImpl implements DataLoadingService {

    private static Random random = new Random();
    private final String TOKEN = "sk.eyJ1IjoiZm9yZXN0eXVyYSIsImEiOiJjanZ6NmZlcDMwbGo0M3ptcmNjcTBrZzg1In0.dFxSaSFyDkgOGBDhNrv2bg";
    private final RestTemplate restTemplate = new RestTemplate();
    private final List<String> getNameOfPlace = Arrays.asList(
            "фора", "сильпо", "лоток", "магазин", "аптека", "сто", "мойка", "шиномнтаж", "остановка", "бар", "кафе", "street", "сервис");
    private final List<String> carServiceImage = Arrays.asList(
            "https://itechua.com/wp-content/uploads/2019/03/28_main.jpg",
            "https://autonews.ua/wp-content/uploads/2016/12/img_3773-1068x712.jpg",
            "https://www.avtovzglyad.ru/media/article/02_3Iqdtan.jpg.740x555_q85_box-132%2C0%2C1199%2C799_crop_detail_upscale.jpg",
            "https://p2.zoon.ru/preview/fyGvggO_PAFQWrcU98UYqw/520x270x85/1/c/5/original_59ee4193c8aca6013966c3b6_5a4bb81a1229c.jpg");
    private final List<String> carWashImage = Arrays.asList(
            "https://moikon.ru/images/mojki-samoobsluzhivaniya.jpg",
            "https://alterainvest.ru/upload/iblock/a75/a751850b141e5763f7efb95cecaedc38.jpg",
            "https://sochi-avto-remont.ru/wp-content/uploads/2015/03/avtomoyka-vyisokogo-davleniya-svoimi-rukami-1.jpg",
            "https://www.slivki.by/znijki-media/w522_322/default/1009921/1520932756_moyka-himchistka-polirovka-avto-minsk-skidka-agroavtotrans-1.jpg");
    private final List<String> tireFittingImage = Arrays.asList(
            "https://www.slivki.by/znijki-media/w522_322/default/1009921/1542738698_shinomontazh-minsk-skidka-shinaminskby-2.jpg",
            "https://www.slivki.by/znijki-media/w522_322/default/1009921/diagnostika-podveski-zamena-masla-besplatno-minsk-skidka-shinaminskby-1.jpg",
            "https://alterainvest.ru/upload/iblock/4eb/4ebbeb6a50ebedfbf6a76b9cd88640fd.jpg",
            "https://autonews.ua/wp-content/uploads/2017/10/shinomontagnoe-oborudovanie-dlya-montaga.jpg");
    private int count = 1;
    @Autowired
    private WorkTimeService workTimeService;
    @Autowired
    private WorkingSpaceService workingSpaceService;
    @Autowired
    private BaseBusinessService businessService;
    @Autowired
    private BusinessCategoryService categoryService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private ServicePriceService servicePriceService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private BaseRecordService recordService;
    @Autowired
    private CarService carService;
    @Autowired
    private BaseBusinessRepository businessRepository;
    @Autowired
    private UserExchangeService userExchangeService;
    @Autowired
    private ClientFacade clientFacade;

    @Override
    @Transactional
    public void createBusiness(String rightTop, String leftBottom) {
        getNameOfPlace.forEach(f -> getLocation(flipCoordinate(rightTop), flipCoordinate(leftBottom), f));
    }

    private String flipCoordinate(String s) {
        return s.substring(s.indexOf(',') + 1).concat(",").concat(s.substring(0, s.indexOf(',')));
    }

    private void getLocation(String rightTop, String leftBottom, String nameOfPlace) {
        String url = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + nameOfPlace + ".json";
        String uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("bbox", leftBottom.concat(",").concat(rightTop))
                .queryParam("limit", "200")
                .queryParam("access_token", TOKEN)
                .build().toString();
        ResponseEntity<MapBoxDto> response = restTemplate.getForEntity(uri, MapBoxDto.class);
        if ((response.getStatusCode().is2xxSuccessful()) && (response.hasBody())) {
            MapBoxDto box = response.getBody();
            if (CollectionUtils.isNotEmpty(box.getFeatures())) {
                List<BusinessCategoryDto> categories = categoryService.getAll();
                createNewBusiness(box.getFeatures(), categories);
            }
        }
    }

    private void createNewBusiness(List<Map<String, Object>> list, List<BusinessCategoryDto> categories) {
        UUID corporationId = SecurityUtil.getUserCorporationIds().get(0);
        list.forEach(f -> {
            String address = (String) f.get("place_name");
            ArrayList<Double> coordinate = (ArrayList<Double>) f.get("center");
            Double longitude = coordinate.get(0);
            Double latitude = coordinate.get(1);
            BusinessCategoryDto category = categories.get(random.nextInt(categories.size()));
            createBusiness(address, longitude, latitude, corporationId, category);
            count++;
        });
    }

    public void createBusiness(String address, Double longitude, Double latitude, UUID corporationId, BusinessCategoryDto category) {
        BaseBusinessDto business = new BaseBusinessDto();
        business.setBusinessCategoryId(category.getId());
        business.setPhone(String.valueOf(380995550000L + count));
        business.setName(category.getName().concat(" #").concat(String.valueOf(count)));
        business.setDescription(category.getDescription());
        business.setAddress(address);
        business.setCorporationId(corporationId);
        business.setLatitude(latitude);
        business.setLongitude(longitude);
        business.setTimeZone(180);
        BaseBusinessDto result = businessService.create(business);
        if (result != null) {
            setMedia(result.getId(), result.getName(), getListByCategory(category.getCode()));
            createWorkingSpace(result.getId(), result.getBusinessCategoryId());
            createWorkingTimes(result.getId(), result.getBusinessCategoryId());
            List<UUID> ids = createServicePrice(result.getId(), result.getBusinessCategoryId());
            createPackage(ids, result.getId());
        }
    }

    public void createWorkingTimes(UUID businessId, UUID businessCategoryId) {
        List<WorkTimeDto> workTimes = new ArrayList<>();
        Arrays.asList(DayOfWeek.values()).forEach(f -> {
            WorkTimeDto workTime = new WorkTimeDto();
            workTime.setBusinessCategoryId(businessCategoryId);
            workTime.setObjectId(businessId);
            workTime.setIsWork(true);
            workTime.setDayOfWeek(f);
            workTime.setFrom(LocalTime.of(random.nextInt(11 - 7 + 1) + 7, 00, 00));
            workTime.setTo(LocalTime.of(random.nextInt(21 - 16 + 1) + 16, 00, 00));
            workTimes.add(workTime);
        });
        workTimeService.create(workTimes);
    }

    public void createWorkingSpace(UUID businessId, UUID businessCategoryId) {
        List<WorkingSpaceDto> workingSpaces = new ArrayList<>();
        int col = random.nextInt(5 - 2 + 1) + 2;
        while (col > 0) {
            WorkingSpaceDto dto = new WorkingSpaceDto();
            dto.setBusinessCategoryId(businessCategoryId);
            dto.setBusinessId(businessId);
            dto.setIndexNumber(col);
            dto.setName("Working space #".concat(String.valueOf(col)));
            dto.setStatusSpace(StatusSpace.FREE);
            workingSpaces.add(dto);
            col--;
        }
        workingSpaceService.create(workingSpaces);
    }

    public List<UUID> createServicePrice(UUID businessId, UUID businessCategoryId) {
        List<ServiceDto> services = serviceService.getAllByBusinessCategoryId(businessCategoryId);
        List<UUID> result = null;
        List<ServicePriceDto> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(services)) {
            List<ServicePriceDto> finalList = list;
            services.forEach(f -> {
                ServicePriceDto servicePrice = new ServicePriceDto();
                servicePrice.setBusinessId(businessId);
                servicePrice.setName(f.getName());
                servicePrice.setDuration(random.nextInt(25 - 10 + 1) + 10);
                servicePrice.setObjectState(ObjectState.ACTIVE);
                servicePrice.setPrice(random.nextInt(100 - 20 + 1) + 20);
                servicePrice.setServiceId(f.getId());
                finalList.add(servicePrice);
            });
        }
        list = servicePriceService.create(list);
        if (CollectionUtils.isNotEmpty(list)) {
            result = list.stream().map(m -> m.getId()).collect(Collectors.toList());
        }
        return result;
    }

    public void createPackage(List<UUID> serviceIds, UUID businessId) {
        if (CollectionUtils.isNotEmpty(serviceIds)) {
            int col = random.nextInt(3 - 1 + 1) + 1;
            while (col > 0) {
                final int serviceCount = random.nextInt(serviceIds.size()) + 1;
                Set<UUID> generated = new HashSet<>();
                for (int i = 0; i < serviceCount; i++) {
                    generated.add(serviceIds.get(random.nextInt(serviceIds.size())));
                }
                PackageDto packageDto = new PackageDto();
                packageDto.setBusinessId(businessId);
                packageDto.setDiscount(col * 5);
                packageDto.setDuration(generated.size() * 10);
                packageDto.setName("Package #".concat(String.valueOf(col)));
                packageDto.setObjectState(ObjectState.ACTIVE);
                packageDto.setServicesIds(new ArrayList<>(generated));
                packageService.create(packageDto);
                col--;
            }
        }
    }

    public void setMedia(UUID businessId, String title, List<String> urls) {
        if (CollectionUtils.isNotEmpty(urls) && (count % 5 != 0)) {
            final int count = random.nextInt(urls.size()) + 1;
            Set<String> images = new HashSet<>();
            for (int i = 0; i < count; i++) {
                images.add(urls.get(random.nextInt(urls.size())));
            }
            List<MediaDto> list = new ArrayList<>();
            images.forEach(f -> {
                MediaDto media = new MediaDto();
                media.setMediaType(MediaType.IMAGE);
                media.setObjectId(businessId);
                media.setUrl(f);
                media.setTitle(title);
                list.add(media);
            });
            mediaService.create(list);
        }
    }

    private List<String> getListByCategory(String category) {
        switch (category) {
            case "CAR_WASH": {
                return carWashImage;
            }
            case "TIRE_FITTING": {
                return tireFittingImage;
            }
            case "CAR_SERVICE": {
                return carServiceImage;
            }
            default:
                return new ArrayList<>();
        }
    }

    //+++++++++++++++++++++++++++++ Records ++++++++++++++++++++++++++++


    @Override
    @Async
    @Transactional
    public void createRecordsByBusinessId(UUID businessId) {
        if (businessId != null) {
            List<CarDto> cars = carService.getAll();
            List<BusinessFullModel> fullModelList = businessService.getFullModelByIds(Arrays.asList(businessId));
            createRecords(cars, fullModelList);
        }
    }

    @Override
    @Async
    @Transactional
    public void createRecords() {
        List<BaseBusinessEntity> baseBusiness = businessRepository.getAllByObjectState(ObjectState.ACTIVE);
        List<CarDto> cars = carService.getAll();
        if (CollectionUtils.isNotEmpty(baseBusiness) && CollectionUtils.isNotEmpty(cars)) {
            List<BusinessFullModel> listBusiness = businessService.getFullModelByIds(baseBusiness.stream().map(m -> m.getId()).collect(Collectors.toList()));

            createRecords(cars, listBusiness);
        }
    }

    @Override
    public void loadClient() {
        List<BaseRecordDto> records = recordService.getAll();
        if (CollectionUtils.isNotEmpty(records)) {
            records = records.stream().filter(i -> ObjectUtils.allNotNull(i.getClientId(), i.getBusinessId())).collect(Collectors.toList());
            Set<UUID> userIds = records.stream().map(BaseRecordDto::getClientId).collect(Collectors.toSet());
            Map<UUID, UserDto> userMap = userExchangeService.findUserMapByIds(userIds);
            records.forEach(record -> {
                clientFacade.addNewClient(userMap.get(record.getClientId()), record.getBusinessId());
            });
        }
    }

    private void createRecords(List<CarDto> cars, List<BusinessFullModel> listBusiness) {
        if (CollectionUtils.isNotEmpty(cars) && CollectionUtils.isNotEmpty(listBusiness)) {
            log.info("Start create record");
            List<BaseRecordDto> newRecords = new ArrayList<>();
            Random random = new Random();

            listBusiness.forEach(business -> {
                LocalDateTime to = LocalDateTime.now().toLocalDate().atStartOfDay().minusDays(10L).plusYears(1L);
                LocalDateTime from = to.plusDays(1L).minusMonths(12);

                int recordPerDay = random.nextInt(5);

                while (from.isBefore(to)) {
                    recordPerDay = recordPerDay - 1;
                    if (recordPerDay <= 0) {
                        from = from.plusDays(1L);
                        recordPerDay = random.nextInt(5);
                    }
                    LocalDateTime startOfWorkingDay = startOfWorkingDay(business, from);
                    if (startOfWorkingDay != null) {
                        CarDto car = getRandomCar(cars);
                        try {
                            BaseRecordDto record = new BaseRecordDto();
                            record.setDescription("Test record");
                            record.setNotificationSend(true);
                            record.setPackageId(getPackageId(business));
                            record.setServicesIds(getServiceIds(business));
                            record.setClientId(car.getUserId());
                            record.setTargetId(car.getId());
                            record.setStatusPay(StatusPay.PAID);
                            record.setPayType(PayType.CASH);
                            record.setStatusProcess(StatusProcess.IN_PROCESS);
                            record.setStatusRecord(StatusRecord.CREATED);
                            record.setBusinessId(business.getId());
                            record.setBegin(startOfWorkingDay);
                            record.setBusinessCategoryId(business.getBusinessCategoryId());
                            BaseRecordDto newRecord = recordService.getFreeTimeForRecord(record, false);
                            newRecords.add(recordService.superCreateRecord(newRecord));
                            log.info("write: " + newRecords.size() + "   " + from);
                        } catch (ClientException e) {
                            recordPerDay = random.nextInt(5);
                            from = from.plusDays(1L);
                        }
                    } else {
                        recordPerDay = random.nextInt(5);
                        from = from.plusDays(1L);
                    }
                }
            });

            if (CollectionUtils.isNotEmpty(newRecords)) {
                newRecords.forEach(f -> {
                    f.setBegin(f.getBegin().minusYears(1L));
                    f.setFinish(f.getFinish().minusYears(1L));
                    f.setStatusRecord(StatusRecord.COMPLETED);
                    f.setStatusProcess(StatusProcess.COMPLETED);
                });
                log.info("start update");
                recordService.update(newRecords);
            }

            log.info("Finish create record");
        }
    }

    private CarDto getRandomCar(List<CarDto> cars) {
        return cars.get(random.nextInt(cars.size()));
    }


    private LocalDateTime startOfWorkingDay(BusinessFullModel business, LocalDateTime from) {
        WorkTimeDto workTime = business.getWorkTimes().stream().filter(f1 -> f1.getIsWork()).filter(f -> f.getDayOfWeek().equals(from.getDayOfWeek())).findFirst().orElse(null);
        if (workTime != null) {
            return LocalDateTime.of(from.toLocalDate(), workTime.getFrom().plusMinutes(10L));
        }
        return null;
    }

    private UUID getPackageId(BusinessFullModel business) {
        if (CollectionUtils.isNotEmpty(business.getPackages())) {
            return business.getPackages().get(random.nextInt(business.getPackages().size())).getId();
        }
        return null;
    }

    private List<UUID> getServiceIds(BusinessFullModel business) {
        List<UUID> result = new ArrayList<>();
        final int count = random.nextInt(business.getServicePrices().size()) + 1;
        for (int i = 0; i < count; i++) {
            result.add(business.getServicePrices().get(random.nextInt(business.getServicePrices().size())).getId());
        }
        return result;
    }

}
