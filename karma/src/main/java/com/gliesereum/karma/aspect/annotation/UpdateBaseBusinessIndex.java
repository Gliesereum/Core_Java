//TODO: remove

//package com.gliesereum.karma.aspect.annotation;
//
//import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import java.util.function.Function;
//
///**
// * @author yvlasiuk
// * @version 1.0
// */
//@Retention(RetentionPolicy.RUNTIME)
//@Target(ElementType.METHOD)
//public @interface UpdateBaseBusinessIndex {
//
//    String fieldToAccessId() default "id";
//
//    Class<?> targetClass() default BaseBusinessDto.class;
//
//    int argumentIndex() default 0;
//
//    AccessObjectType accessObjectType() default AccessObjectType.RETURN_VALUE;
//
//    IdType idType() default IdType.BUSINESS_ID;
//
//
//    enum AccessObjectType {
//        RETURN_VALUE,
//        ARGUMENTS
//    }
//
//    enum IdType {
//        BUSINESS_ID,
//        PRICE_ID
//    }
//}
