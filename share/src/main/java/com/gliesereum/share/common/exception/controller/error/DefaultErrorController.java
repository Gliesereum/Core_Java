package com.gliesereum.share.common.exception.controller.error;

import com.gliesereum.share.common.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ENDPOINT_NOT_FOUND;
import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.UNKNOWN_SERVER_EXCEPTION;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@RestController
public class DefaultErrorController extends AbstractErrorController {

    @Value("${error.path:/error}")
    private String errorPath;

    public DefaultErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @Override
    public String getErrorPath() {
        return errorPath;
    }

    @RequestMapping(value = "${error.path:/error}", produces = "application/json;charset=UTF-8")
    public ResponseEntity error(HttpServletRequest request) throws Exception {
        throw prepareResponse(request);
    }

    protected Exception prepareResponse(HttpServletRequest request) {
        Exception exc = (Exception) request.getAttribute("javax.servlet.error.exception");
        if (exc == null) {
            Object attribute = request.getAttribute("javax.servlet.error.status_code");
            Object requestAttribute = request.getAttribute("javax.servlet.error.request_uri");
            String requestPath = null;
            if (requestAttribute instanceof String) {
                requestPath = (String) requestAttribute;
            }
            if ((attribute instanceof Integer) && ((Integer) attribute) == 404) {
                exc = new CustomException(ENDPOINT_NOT_FOUND, requestPath);
            } else {
                exc = new CustomException(UNKNOWN_SERVER_EXCEPTION, requestPath);
            }
        }
        return exc;
    }

}
