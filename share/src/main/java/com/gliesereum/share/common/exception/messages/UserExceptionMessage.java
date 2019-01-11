package com.gliesereum.share.common.exception.messages;

/**
 * @author vitalij
 * @since 12/10/2018
 */
public class UserExceptionMessage {

    public static final ExceptionMessage USER_NOT_FOUND = new ExceptionMessage(1010, 400, "User not found ");
    public static final ExceptionMessage USER_NOT_AUTHENTICATION = new ExceptionMessage(1011, 400, "User not authentication ");
    public static final ExceptionMessage USER_ERROR_CHANGE_FIRST_NAME = new ExceptionMessage(1012, 400, "You can't change first name. Contact administrator");
    public static final ExceptionMessage USER_ERROR_CHANGE_LAST_NAME = new ExceptionMessage(1013, 400, "You can't change last name. Contact administrator");
    public static final ExceptionMessage UPL_AVATAR_IS_NOT_VALID = new ExceptionMessage(1014, 500, "Url avatar is not valid");
    public static final ExceptionMessage UPL_COVER_IS_NOT_VALID = new ExceptionMessage(1015, 500, "Url cover is not valid");
    public static final ExceptionMessage DONT_FOUND_DEPENDENCY_USER_AND_CORPORATION = new ExceptionMessage(1016, 400, "Dnot found dependency user and corporation");
    public static final ExceptionMessage USER_NOT_VERIFIED = new ExceptionMessage(1017, 400, "User not VERIFIED ");
    public static final ExceptionMessage USER_IN_BAN = new ExceptionMessage(1018, 400, "User in ban ");
    public static final ExceptionMessage USER_DONT_HAVE_PERMISSION_TO_CORPORATION = new ExceptionMessage(1019, 400, "Current user don't have permission to action on corporation");
    public static final ExceptionMessage CORPORATION_NOT_FOUND = new ExceptionMessage(1020, 400, "Corporation not found");
    public static final ExceptionMessage DONT_HAVE_PERMISSION_TO_CHANGE_KYS_STATUS = new ExceptionMessage(1021, 400, "Don't have permission to change kyc status");
    public static final ExceptionMessage DONT_HAVE_PERMISSION_TO_CHANGE_VERIFIED_STATUS = new ExceptionMessage(1022, 400, "Don't have permission to change verified status");
    public static final ExceptionMessage KYC_STATUS_IS_EMPTY = new ExceptionMessage(1023, 400, "Verified status is empty");
    public static final ExceptionMessage CORPORATION_ID_IS_EMPTY = new ExceptionMessage(1024, 400, "Corporation id is empty");

}
