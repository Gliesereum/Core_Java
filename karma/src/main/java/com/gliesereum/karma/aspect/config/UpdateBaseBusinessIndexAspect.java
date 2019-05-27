//TODO: remove

//package com.gliesereum.karma.aspect.config;
//
//import com.gliesereum.karma.aspect.annotation.UpdateBaseBusinessIndex;
//import com.gliesereum.karma.service.es.BusinessEsService;
//import com.gliesereum.karma.service.service.ServicePriceService;
//import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
//import org.apache.commons.lang3.NotImplementedException;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.util.ReflectionUtils;
//
//import java.lang.reflect.Field;
//import java.util.UUID;
//
//
///**
// * @author yvlasiuk
// * @version 1.0
// */
//
//@Component
//@Aspect
//public class UpdateBaseBusinessIndexAspect {
//
//    @Autowired
//    private BusinessEsService businessEsService;
//
//    @Autowired
//    private ServicePriceService servicePriceService;
//
//    @AfterReturning(value = "@annotation(updateBaseBusinessIndex)", returning = "retVal")
//    public void updateBaseBusinessIndex(JoinPoint joinPoint,
//                                        UpdateBaseBusinessIndex updateBaseBusinessIndex,
//                                        Object retVal) {
//        UpdateBaseBusinessIndex.AccessObjectType accessObjectType = updateBaseBusinessIndex.accessObjectType();
//        Object object;
//        UUID targetId;
//        if (accessObjectType.equals(UpdateBaseBusinessIndex.AccessObjectType.ARGUMENTS)) {
//            object = joinPoint.getArgs()[updateBaseBusinessIndex.argumentIndex()];
//        } else {
//            object = retVal;
//        }
//        Class<?> targetClass = updateBaseBusinessIndex.targetClass();
//        if (!targetClass.isAssignableFrom(object.getClass())) {
//            throw new NotImplementedException("Invalid targetClass");
//        }
//        if (targetClass.isAssignableFrom(UUID.class)) {
//            targetId = (UUID) object;
//        } else {
//            Field field = ReflectionUtils.findField(targetClass, updateBaseBusinessIndex.fieldToAccessId());
//            if ((field == null) || !field.getT().isAssignableFrom(UUID.class)) {
//                throw new NotImplementedException("Invalid field type");
//            }
//            targetId = (UUID) ReflectionUtils.getField(field, object);
//
//        }
//        UUID businessId = getBusinessId(targetId, updateBaseBusinessIndex.idType());
//        if (businessId != null) {
//            businessEsService.indexAsync(businessId);
//        }
//    }
//
//    private UUID getBusinessId(UUID id, UpdateBaseBusinessIndex.IdType idType) {
//        UUID result = null;
//        if (idType.equals(UpdateBaseBusinessIndex.IdType.BUSINESS_ID)) {
//            result = id;
//        } else {
//            if (idType.equals(UpdateBaseBusinessIndex.IdType.PRICE_ID)) {
//                ServicePriceDto price = servicePriceService.getById(id);
//                if (price != null) {
//                    result = price.getBusinessId();
//                }
//            }
//        }
//        return result;
//    }
//}
