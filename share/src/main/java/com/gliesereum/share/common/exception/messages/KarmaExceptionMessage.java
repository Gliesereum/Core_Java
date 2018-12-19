package com.gliesereum.share.common.exception.messages;

/**
 * @author vitalij
 * @since 12/10/2018
 */
public class KarmaExceptionMessage {

    public static final ExceptionMessage CAR_NOT_FOUND = new ExceptionMessage(1400, 400, "Car not found");
    public static final ExceptionMessage SERVICE_CLASS_NOT_FOUND = new ExceptionMessage(1410, 400, "Service class not found");
    public static final ExceptionMessage DONT_HAVE_PERMISSION_TO_ACTION_CARWASH = new ExceptionMessage(1420, 401, "Current user don't have permission to action this carwash");
    public static final ExceptionMessage CARWASH_NOT_FOUND = new ExceptionMessage(1421, 404, "Carwash not found");
    public static final ExceptionMessage CARWASH_ID_EMPTY = new ExceptionMessage(1422, 404, "Carwash id is empty");
    public static final ExceptionMessage DONT_HAVE_PERMISSION_TO_CREATE_CARWASH = new ExceptionMessage(1423, 401, "Current user don't have permission to create carwash");

    public static final ExceptionMessage DONT_HAVE_PERMISSION_TO_ACTION_SERVICE = new ExceptionMessage(1424, 401, "Current user don't have permission to action this service");

    public static final ExceptionMessage SERVICE_NOT_FOUND = new ExceptionMessage(1425, 404, "Service not found");
    public static final ExceptionMessage SERVICE_NOT_CHOOSE = new ExceptionMessage(1426, 401, "Service not choose");
    public static final ExceptionMessage PACKAGE_NOT_FOUND = new ExceptionMessage(1427, 404, "Package not found ");

    public static final ExceptionMessage WORKING_SPACE_NOT_FOUND_IN_THIS_CARWASH = new ExceptionMessage(1428, 404, "CarWash don't have this working space ");
    public static final ExceptionMessage WORKING_SPACE_ID_EMPTY = new ExceptionMessage(1429, 401, "Working space id is null ");
    public static final ExceptionMessage NOT_ENOUGH_TIME_FOR_RECORD = new ExceptionMessage(1430, 401, "Not enough time for create record, choose another time");
    public static final ExceptionMessage RECORD_NOT_FOUND = new ExceptionMessage(1431, 404, "Record not found");

    public static final ExceptionMessage TIME_BEGIN_EMPTY = new ExceptionMessage(1432, 401, "Time begin is empty");
    public static final ExceptionMessage WORKING_SPACE_NOT_FOUND = new ExceptionMessage(1433, 404, "Working space not found");

    public static final ExceptionMessage DONT_HAVE_PERMISSION_TO_ACTION_RECORD = new ExceptionMessage(1434, 401, "Current user don't have permission to action this record");

    public static final ExceptionMessage ANONYMOUS_CANT_COMMENT = new ExceptionMessage(1440, 401, "Anonymous user can't comment");
    public static final ExceptionMessage COMMENT_FOR_USER_EXIST = new ExceptionMessage(1441, 400, "Comment for current user exist");
    public static final ExceptionMessage CURRENT_USER_CANT_EDIT_THIS_COMMENT = new ExceptionMessage(1442, 401, "Current user cant't edit this comment");
    public static final ExceptionMessage COMMENT_NOT_FOUND = new ExceptionMessage(1443, 404, "Comment not found");


    public static final ExceptionMessage MEDIA_NOT_FOUND_BY_ID = new ExceptionMessage(1450, 404, "Media not found by id");

}
