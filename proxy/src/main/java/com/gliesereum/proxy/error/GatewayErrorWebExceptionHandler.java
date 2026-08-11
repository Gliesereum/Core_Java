package com.gliesereum.proxy.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gliesereum.share.common.exception.CustomException;
import com.gliesereum.share.common.exception.messages.ExceptionMessage;
import com.gliesereum.share.common.exception.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.UNKNOWN_SERVER_EXCEPTION;

/**
 * Renders the error body callers already depend on:
 * {"code":1310,"message":"ApplicationId required","path":"/api/...","additional":null,"timestamp":...}
 *
 * Replaces both the servlet ExceptionHandlerFilter and ZuulErrorController.
 * Ordered ahead of Boot's DefaultErrorWebExceptionHandler (-1), which would
 * otherwise answer with its own generic shape.
 */
@Component
@Order(-2)
public class GatewayErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GatewayErrorWebExceptionHandler.class);

    private final ObjectMapper objectMapper;

    public GatewayErrorWebExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }

        String path = exchange.getRequest().getURI().getRawPath();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setPath(path);
        errorResponse.setTimestamp(LocalDateTime.now());

        int statusCode;
        if (ex instanceof CustomException) {
            CustomException customException = (CustomException) ex;
            errorResponse.setCode(customException.getErrorCode());
            errorResponse.setMessage(customException.getMessage());
            statusCode = customException.getHttpCode();
        } else if (ex instanceof ResponseStatusException) {
            // Raised by the gateway itself, e.g. no route matched.
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            statusCode = responseStatusException.getStatus().value();
            errorResponse.setCode(statusCode);
            errorResponse.setMessage(responseStatusException.getReason() != null
                    ? responseStatusException.getReason()
                    : responseStatusException.getStatus().getReasonPhrase());
        } else {
            ExceptionMessage unknown = UNKNOWN_SERVER_EXCEPTION;
            errorResponse.setCode(unknown.getErrorCode());
            errorResponse.setMessage(ex.getMessage());
            statusCode = unknown.getHttpCode();
        }

        return write(exchange, errorResponse, statusCode, ex);
    }

    private Mono<Void> write(ServerWebExchange exchange, ErrorResponse errorResponse, int statusCode, Throwable ex) {
        HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
        if (httpStatus.is5xxServerError()) {
            LOG.error("{} error - errorCode: {}, message: {}, path: {}",
                    statusCode, errorResponse.getCode(), errorResponse.getMessage(), errorResponse.getPath(), ex);
        }
        exchange.getResponse().setStatusCode(httpStatus);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        byte[] body;
        try {
            body = objectMapper.writeValueAsBytes(errorResponse);
        } catch (Exception serialisationFailure) {
            LOG.error("Could not serialise the error response for {}", errorResponse.getPath(), serialisationFailure);
            body = ("{\"code\":" + errorResponse.getCode() + "}").getBytes(StandardCharsets.UTF_8);
        }
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
