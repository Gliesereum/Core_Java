package com.gliesereum.karma.aspect.config;

import com.gliesereum.karma.service.es.BusinessEsService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author yvlasiuk
 * @version 1.0
 */

@Component
@Aspect
public class UpdateCarWashIndexAspect {

    @Autowired
    private BusinessEsService businessEsService;

    @AfterReturning("@annotation(com.gliesereum.karma.aspect.annotation.UpdateCarWashIndex)")
    public void updateCarWashIndex() {
        businessEsService.indexAllAsync();
    }


}
