package com.gliesereum.proxy.controller;

import com.gliesereum.share.common.exception.CustomException;
import com.netflix.client.ClientException;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.*;

/**
 * @author yvlasiuk
 * @version 1.0
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
            Object attribute = request.getAttribute("javax.servlet.error.status_code");
            if ((attribute instanceof Integer) && ((Integer) attribute) == 404) {
                exc = new CustomException(ENDPOINT_NOT_FOUND);
            } else {
                exc = new CustomException(UNKNOWN_SERVER_EXCEPTION);
            }
        }
        if (exc.getCause() instanceof CustomException) {
            throw (CustomException) exc.getCause();
        }
        if (exc instanceof ZuulException) {
            ZuulException zuulException = (ZuulException) exc;
            Throwable cause = zuulException.getCause();
            if (cause instanceof ClientException) {
                exc = new CustomException(SERVICE_NOT_AVAILABLE);
            }
        }
        throw exc;
    }
}
