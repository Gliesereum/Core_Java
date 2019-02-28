package com.gliesereum.share.common.exception.messages;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class KycExceptionMessage {

    public static final ExceptionMessage KYC_REQUEST_TYPE_MISSED = new ExceptionMessage(1080, 400, "KYC request type missed");
    public static final ExceptionMessage KYC_OBJECT_ID_MISSED = new ExceptionMessage(1081, 400, "KYC objectId missed");
    public static final ExceptionMessage KYC_REQUEST_CANT_TO_ANOTHER_USER = new ExceptionMessage(1082, 400, "Can't create request to another user");
    public static final ExceptionMessage KYC_FIELDS_IS_MISSED = new ExceptionMessage(1083, 400, "KYC request fields is missed");
    public static final ExceptionMessage KYC_REQUIRED_FIELD_IS_MISSED = new ExceptionMessage(1084, 400, "KYC request required field is missed");
    public static final ExceptionMessage KYC_FIELD_FILE_ID_IS_NOT_UUID = new ExceptionMessage(1085, 400, "KYC request required fileId is not UUID");
    public static final ExceptionMessage KYC_REQUEST_FOR_OBJECT_EXIST = new ExceptionMessage(1086, 400, "Not rejected KYC request for this object exist");
    public static final ExceptionMessage KYC_REQUEST_PASSED_CANNOT_UPDATE = new ExceptionMessage(1087, 400, "KYC request with status passed cannot be updated");
    public static final ExceptionMessage KYC_REQUEST_REJECTED_CANNOT_UPDATE = new ExceptionMessage(1088, 400, "KYC request with status rejected cannot be updated");
    public static final ExceptionMessage KYC_REQUEST_ONLY_SENT_TO_REWORK_CAN_UPDATE = new ExceptionMessage(1089, 400, "Only KYC request with status sent to rework can be updated");


}
