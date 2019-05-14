package com.gliesereum.share.common.exception.messages;

/**
 * @author vitalij
 */
public class KarmaExceptionMessage {

    public static final ExceptionMessage CAR_NOT_FOUND = new ExceptionMessage(1400, 400, "Car not found");
    public static final ExceptionMessage SERVICE_CLASS_NOT_FOUND = new ExceptionMessage(1410, 400, "Service class not found");
    public static final ExceptionMessage SERVICE_PRICE_NOT_FOUND = new ExceptionMessage(1411, 400, "Service price not found");
    public static final ExceptionMessage DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS = new ExceptionMessage(1420, 401, "Current user don't have permission to action this business");
    public static final ExceptionMessage BUSINESS_NOT_FOUND = new ExceptionMessage(1421, 404, "Business not found");
    public static final ExceptionMessage BUSINESS_ID_EMPTY = new ExceptionMessage(1422, 404, "Business id is empty");
    public static final ExceptionMessage DONT_HAVE_PERMISSION_TO_CREATE_BUSINESS = new ExceptionMessage(1423, 401, "Current user don't have permission to create business");

    public static final ExceptionMessage DONT_HAVE_PERMISSION_TO_ACTION_SERVICE = new ExceptionMessage(1424, 401, "Current user don't have permission to action this service");

    public static final ExceptionMessage SERVICE_NOT_FOUND = new ExceptionMessage(1425, 400, "Service not found");
    public static final ExceptionMessage SERVICE_NOT_CHOOSE = new ExceptionMessage(1426, 400, "Service not choose");
    public static final ExceptionMessage PACKAGE_NOT_FOUND = new ExceptionMessage(1427, 400, "Package not found ");

    public static final ExceptionMessage WORKING_SPACE_NOT_FOUND_IN_THIS_BUSINESS = new ExceptionMessage(1428, 404, "Business don't have this working space ");
    public static final ExceptionMessage WORKING_SPACE_ID_EMPTY = new ExceptionMessage(1429, 400, "Working space id is null ");
    public static final ExceptionMessage NOT_ENOUGH_TIME_FOR_RECORD = new ExceptionMessage(1430, 400, "Not enough time for create record, choose another time");
    public static final ExceptionMessage RECORD_NOT_FOUND = new ExceptionMessage(1431, 404, "Record not found");

    public static final ExceptionMessage TIME_BEGIN_EMPTY = new ExceptionMessage(1432, 400, "Time begin is empty");
    public static final ExceptionMessage WORKING_SPACE_NOT_FOUND = new ExceptionMessage(1433, 404, "Working space not found");
    public static final ExceptionMessage BUSINESS_NOT_WORK_THIS_DAY = new ExceptionMessage(1434, 400, "Business don't work this day");
    public static final ExceptionMessage BUSINESS_NOT_WORK_THIS_TIME = new ExceptionMessage(1435, 400, "Business don't work this time");

    public static final ExceptionMessage WORKING_TIME_EXIST_IN_THIS_BUSINESS = new ExceptionMessage(1436, 400, "Time working already exist in this business");
    public static final ExceptionMessage WORKING_TIME_NOT_FOUND = new ExceptionMessage(1437, 401, "Time working not found");

    public static final ExceptionMessage DONT_HAVE_PERMISSION_TO_ACTION_RECORD = new ExceptionMessage(1438, 404, "Current user don't have permission to action this record");

    public static final ExceptionMessage TIME_BEGIN_PAST = new ExceptionMessage(1439, 400, "Try to choose past time");


    public static final ExceptionMessage ANONYMOUS_CANT_COMMENT = new ExceptionMessage(1440, 401, "Anonymous user can't comment");
    public static final ExceptionMessage COMMENT_FOR_USER_EXIST = new ExceptionMessage(1441, 400, "Comment for current user exist");
    public static final ExceptionMessage CURRENT_USER_CANT_EDIT_THIS_COMMENT = new ExceptionMessage(1442, 401, "Current user cant't edit this comment");
    public static final ExceptionMessage COMMENT_NOT_FOUND = new ExceptionMessage(1443, 404, "Comment not found");


    public static final ExceptionMessage MEDIA_NOT_FOUND_BY_ID = new ExceptionMessage(1450, 404, "Media not found by id");
    public static final ExceptionMessage PACKAGE_NOT_HAVE_SERVICE = new ExceptionMessage(1451, 400, "Package dont have any services ");

    public static final ExceptionMessage CORPORATION_ID_REQUIRED_FOR_THIS_ACTION = new ExceptionMessage(1452, 400, "Corporation id required for this action");

    public static final ExceptionMessage CAR_ID_EMPTY = new ExceptionMessage(1453, 404, "Car id is empty");
    public static final ExceptionMessage SERVICE_PRICE_NOT_FOUND_IN_BUSINESS = new ExceptionMessage(1454, 404, "Service price not found in this business");
    public static final ExceptionMessage TARGET_ID_IS_EMPTY = new ExceptionMessage(1455, 400, "Target id is empty");
    public static final ExceptionMessage STATUS_TYPE_IS_EMPTY = new ExceptionMessage(1456, 400, "Status type is empty");

    public static final ExceptionMessage FILTER_ATTRIBUTE_NOT_FOUND = new ExceptionMessage(1457, 400, "Filter attribute not found");
    public static final ExceptionMessage FILTER_ATTRIBUTE_NOT_FOUND_BY_SERVICE_TYPE = new ExceptionMessage(1458, 400, "Filter attribute not found by this service type");
    public static final ExceptionMessage FILTER_ATTRIBUTE_NOT_FOUND_WITH_PRICE = new ExceptionMessage(1459, 400, "Filter attribute with price not found");

    public static final ExceptionMessage SERVICE_ID_IS_EMPTY = new ExceptionMessage(1460, 400, "Service id is empty");
    public static final ExceptionMessage WORKING_SPACE_ID_IS_EMPTY = new ExceptionMessage(1461, 400, "Working space id is empty");
    public static final ExceptionMessage WORKER_ID_IS_EMPTY = new ExceptionMessage(1462, 400, "Worker id is empty");
    public static final ExceptionMessage WORKER_NOT_FOUND = new ExceptionMessage(1463, 400, "Worker not found");

    public static final ExceptionMessage SERVICE_CLASS_ID_IS_EMPTY = new ExceptionMessage(1464, 400, "Service class id is empty");
    public static final ExceptionMessage PRICE_ID_IS_EMPTY = new ExceptionMessage(1465, 400, "Price id is empty");
    public static final ExceptionMessage PAR_SERVICE_CLASS_ID_AND_PRICE_ID_EXIST = new ExceptionMessage(1466, 400, "Par service id and price id exist");
    public static final ExceptionMessage PACKAGE_ID_IS_EMPTY = new ExceptionMessage(1467, 400, "Package id is empty");
    public static final ExceptionMessage PAR_SERVICE_CLASS_ID_AND_PACKAGE_ID_EXIST = new ExceptionMessage(1468, 400, "Par service id and package id exist");
    public static final ExceptionMessage PAR_CAR_ID_AND_FILTER_ATTRIBUTE_ID_EXIST = new ExceptionMessage(1469, 400, "Par car id and attribute id exist");
    public static final ExceptionMessage FILTER_ATTRIBUTE_ID_IS_EMPTY = new ExceptionMessage(1470, 400, "Filter attribute id is empty");
    public static final ExceptionMessage CAN_NOT_CHANGE_STATUS_CANCELED_OR_COMPLETED_RECORD = new ExceptionMessage(1471, 400, "You can't change status in canceled or completed record");
    public static final ExceptionMessage ALL_OBJECT_ID_NOT_EQUALS = new ExceptionMessage(1472, 400, "All object id must be equals");

    public static final ExceptionMessage BUSINESS_CATEGORY_BY_CODE_EXIST = new ExceptionMessage(1473, 400, "Business category with this code exist");
    public static final ExceptionMessage BUSINESS_CATEGORY_ID_EMPTY = new ExceptionMessage(1474, 400, "Business category id empty");
    public static final ExceptionMessage BUSINESS_CATEGORY_NOT_FOUND = new ExceptionMessage(1475, 400, "Business category not found");
    public static final ExceptionMessage DIFFERENT_BUSINESS_OR_CATEGORY_OF_BUSINESS = new ExceptionMessage(1476, 400, "You can't create working stace for different business or category of business");

    public static final ExceptionMessage WORKING_SPACE_INDEX_NUMBER_EXIST = new ExceptionMessage(1477, 400, "Working space index number exist");


}
