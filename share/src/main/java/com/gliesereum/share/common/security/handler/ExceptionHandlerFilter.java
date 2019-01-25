package com.gliesereum.share.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gliesereum.share.common.exception.CustomException;
import com.gliesereum.share.common.exception.messages.ExceptionMessage;
import com.gliesereum.share.common.exception.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.UNKNOWN_SERVER_EXCEPTION;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerFilter.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException ex) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(ex.getErrorCode());
            errorResponse.setMessage(ex.getMessage());
            errorResponse.setPath(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath());
            errorResponse.setTimestamp(LocalDateTime.now());
            buildResponse(errorResponse, ex.getHttpCode(), ex, response);
        } catch (Exception ex) {
            ExceptionMessage unknownServerException = UNKNOWN_SERVER_EXCEPTION;
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode(unknownServerException.getErrorCode());
            errorResponse.setMessage(ex.getMessage());
            errorResponse.setPath(ServletUriComponentsBuilder.fromCurrentRequest().build().getPath());
            errorResponse.setTimestamp(LocalDateTime.now());
            buildResponse(errorResponse, unknownServerException.getHttpCode(), ex, response);
        }
    }

    private void buildResponse(ErrorResponse errorResponse, int statusCode, Exception ex, HttpServletResponse response) throws IOException {
        HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
        if (httpStatus.is5xxServerError()) {
            LOG.error("{} error - errorCode: {}, message: {}, path: {}", statusCode, errorResponse.getCode(), errorResponse.getMessage(), errorResponse.getPath(), ex);
        }
        response.setHeader("Content-type", MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
