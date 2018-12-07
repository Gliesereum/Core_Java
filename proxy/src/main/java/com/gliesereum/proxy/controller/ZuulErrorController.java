package com.gliesereum.proxy.controller;

import com.gliesereum.share.common.exception.CustomException;
import com.gliesereum.share.common.exception.messages.CommonExceptionMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 18/10/2018
 */

@RestController
public class ZuulErrorController extends AbstractErrorController {

    @Value("${error.path:/error}")
    private String errorPath;

    public ZuulErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @Override
    public String getErrorPath() {
        return errorPath;
    }

    @RequestMapping(value = "${error.path:/error}", produces = "application/json;charset=UTF-8")
    public ResponseEntity error(HttpServletRequest request) throws Exception {
        Exception exc = (Exception) request.getAttribute("javax.servlet.error.exception");
        if (exc == null) {
            exc = new CustomException(CommonExceptionMessage.UNKNOWN_SERVER_EXCEPTION);
        }
        if (exc.getCause() instanceof CustomException) {
            throw (CustomException) exc.getCause();
        }
        throw exc;
    }
}
