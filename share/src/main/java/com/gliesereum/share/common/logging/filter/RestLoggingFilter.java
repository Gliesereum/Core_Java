package com.gliesereum.share.common.logging.filter;

import com.gliesereum.share.common.logging.model.RequestInfo;
import com.gliesereum.share.common.logging.service.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 06/11/2018
 */
@Slf4j
@Component
public class RestLoggingFilter extends OncePerRequestFilter {

    @Autowired
    private LoggingService loggingService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } catch (Exception ex) {
            processLog(getRequestInfo(startTime, System.currentTimeMillis(), wrappedRequest, wrappedResponse, true, ex));
            throw ex;
        }
        processLog(getRequestInfo(startTime, System.currentTimeMillis(), wrappedRequest, wrappedResponse, false, null));
        wrappedResponse.copyBodyToResponse();
    }

    private void processLog(RequestInfo requestInfo) {
        loggingService.publishingObject(requestInfo);
    }

    private RequestInfo getRequestInfo(Long startTime, Long endTime, ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper, boolean isError, Exception ex) {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setStartTime(startTime);
        requestInfo.setEndTime(endTime);
        requestInfo.setUri(requestWrapper.getRequestURI());
        requestInfo.setMethod(requestWrapper.getMethod());
        requestInfo.setHeaders(getHeaders(requestWrapper));
        requestInfo.setParameters(requestWrapper.getParameterMap());
        requestInfo.setRequestBody(getBody(requestWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding()));
        if (isError) {
            requestInfo.setError(true);
            requestInfo.setErrorMessage(ex.getMessage());
        } else {
            requestInfo.setResponseBody(getBody(responseWrapper.getContentAsByteArray(), responseWrapper.getCharacterEncoding()));
        }
        return requestInfo;
    }

    private String getBody(byte[] contentAsByte, String characterEncoding) {
        String body = null;
        try {
            body = IOUtils.toString(contentAsByte, characterEncoding);
        } catch (IOException e) {
            log.warn("Error while read body", e);
        }
        return body;
    }

    private Map<String, String> getHeaders(ContentCachingRequestWrapper request) {
        Map<String, String> result = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                result.put(headerName, request.getHeader(headerName));
            }
        }
        return result;
    }


}
