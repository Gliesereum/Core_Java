package com.gliesereum.share.common.exception.messages;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class CommonExceptionMessage {

    public static final ExceptionMessage ID_NOT_SPECIFIED = new ExceptionMessage(1000, 400, "Id not specified");
    public static final ExceptionMessage VALIDATION_ERROR = new ExceptionMessage(1001, 400, "Validation error");
    public static final ExceptionMessage NOT_VALID_URI = new ExceptionMessage(1002, 400, "Not valid uri");
    public static final ExceptionMessage BODY_INVALID = new ExceptionMessage(1003, 400, "Request body is invalid");
    public static final ExceptionMessage NOT_EXIST_BY_ID = new ExceptionMessage(1004, 400, "Not exist by id");
    public static final ExceptionMessage METHOD_NOT_SUPPORTED = new ExceptionMessage(1005, 405, "Method not supported");
    public static final ExceptionMessage INVALID_REQUEST_PARAMS = new ExceptionMessage(1006, 400, "Invalid request params");
    public static final ExceptionMessage DONT_HAVE_ANY_PERMISSION = new ExceptionMessage(1040, 401, "Current user don't have any permission");
    public static final ExceptionMessage DONT_HAVE_PERMISSION_TO_MODULE = new ExceptionMessage(1041, 401, "Current user don't have permission to module");
    public static final ExceptionMessage DONT_HAVE_PERMISSION_TO_ENDPOINT = new ExceptionMessage(1042, 401, "Current user don't have permission to endpoint");
    public static final ExceptionMessage MODULE_NOT_ACTIVE = new ExceptionMessage(1043, 403, "Module not active");
    public static final ExceptionMessage ENDPOINT_NOT_ACTIVE = new ExceptionMessage(1044, 403, "Endpoint not active");
    public static final ExceptionMessage ENDPOINT_NOT_FOUND = new ExceptionMessage(1045, 404, "Endpoint not found");
    public static final ExceptionMessage USER_IS_ANONYMOUS = new ExceptionMessage(1046, 401, "User is anonymous, not supported for anonymous user");
    public static final ExceptionMessage SERVICE_TYPE_IS_EMPTY = new ExceptionMessage(1047, 401, "Service type is empty");

    public static final ExceptionMessage UNKNOWN_SERVER_EXCEPTION = new ExceptionMessage(9000, 500, "Server error");
    public static final ExceptionMessage SERVICE_NOT_AVAILABLE = new ExceptionMessage(9001, 500, "Service not available now");


}
