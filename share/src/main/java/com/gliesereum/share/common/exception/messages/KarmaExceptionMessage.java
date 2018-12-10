package com.gliesereum.share.common.exception.messages;

/**
 * @author vitalij
 * @since 12/10/2018
 */
public class KarmaExceptionMessage {

    public static final ExceptionMessage CAR_NOT_FOUND = new ExceptionMessage(1400, 400, "Car not found");
    public static final ExceptionMessage SERVICE_CLASS_NOT_FOUND = new ExceptionMessage(1410, 400, "Service class not found");
    public static final ExceptionMessage DONT_HAVE_PERMISSION_TO_EDIT_CARWASH = new ExceptionMessage(1420, 401, "Current user don't have permission to edit carwash");


    public static final ExceptionMessage MEDIA_NOT_FOUND_BY_ID = new ExceptionMessage(1450, 404, "Media not found by id");

}
