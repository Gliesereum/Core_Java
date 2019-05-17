package com.gliesereum.karma.config.es.mapper;

import com.github.vanroy.springdata.jest.exception.JestElasticsearchException;
import com.github.vanroy.springdata.jest.mapper.ErrorMapper;
import com.google.gson.JsonPrimitive;
import io.searchbox.action.Action;
import io.searchbox.client.JestResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Slf4j
public class CustomErrorMapper implements ErrorMapper {

    private static <T extends JestResult> String getMessage(T result) {
        if (result.getJsonObject() == null) {
            return null;
        }
        JsonPrimitive message = result.getJsonObject().getAsJsonPrimitive("message");
        if (message == null) {
            return null;
        }
        return message.getAsString();
    }

    private static boolean isSuccessfulResponse(int statusCode) {
        return statusCode < 300 || statusCode == 404;
    }

    @Override
    public void mapError(Action action, JestResult result, boolean acceptNotFound) {

        if (!result.isSucceeded()) {

            String errorMessage = String.format("Cannot execute jest action , response code : %s , error : %s , message : %s", result.getResponseCode(), result.getErrorMessage(), getMessage(result));

            if (acceptNotFound && isSuccessfulResponse(result.getResponseCode())) {
                log.debug(errorMessage);
            } else {
                log.error(errorMessage + " \n Response: \n {}", result.getJsonString());

                throw new JestElasticsearchException(errorMessage, result);
            }
        }
    }
}
