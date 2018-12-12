package com.gliesereum.share.common.exception.messages;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 12/10/2018
 */
public class CommonExceptionMessage {

    public static final ExceptionMessage ID_NOT_SPECIFIED = new ExceptionMessage(1000, 400, "Id not specified");
    public static final ExceptionMessage UNKNOWN_SERVER_EXCEPTION = new ExceptionMessage(9000, 500, "Server error");
    public static final ExceptionMessage VALIDATION_ERROR = new ExceptionMessage(1001, 400, "Validation error");
    public static final ExceptionMessage NOT_VALID_URI = new ExceptionMessage(1002, 400, "Not valid uri");
    public static final ExceptionMessage BODY_REQUIRED = new ExceptionMessage(1003, 400, "Required request body is missing");
    public static final ExceptionMessage DONT_HAVE_ANY_PERMISSION = new ExceptionMessage(1040, 401, "Current user don't have any permission");
    public static final ExceptionMessage DONT_HAVE_PERMISSION_TO_MODULE = new ExceptionMessage(1041, 401, "Current user don't have permission to module");
    public static final ExceptionMessage DONT_HAVE_PERMISSION_TO_ENDPOINT = new ExceptionMessage(1042, 401, "Current user don't have permission to endpoint");
    public static final ExceptionMessage MODULE_NOT_ACTIVE = new ExceptionMessage(1043, 403, "Module not active");
    public static final ExceptionMessage ENDPOINT_NOT_ACTIVE = new ExceptionMessage(1044, 403, "Endpoint not active");
    public static final ExceptionMessage USER_IS_ANONYMOUS = new ExceptionMessage(1045, 401, "User is anonymous");


}
