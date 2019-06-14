package com.gliesereum.share.common.exception.handler;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Collection;
import java.util.Map;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@ControllerAdvice
public class RestEmptyBodyHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return Object.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            response.setStatusCode(HttpStatus.NO_CONTENT);
        } else {
            if ((body instanceof Collection) && ((Collection) body).isEmpty()) {
                response.setStatusCode(HttpStatus.NO_CONTENT);
            }
            if ((body instanceof Map) && ((Map) body).isEmpty()) {
                response.setStatusCode(HttpStatus.NO_CONTENT);
            }
        }

        return body;
    }
}
