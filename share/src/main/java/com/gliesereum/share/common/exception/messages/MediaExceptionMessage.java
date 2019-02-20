package com.gliesereum.share.common.exception.messages;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class MediaExceptionMessage {

    public static final ExceptionMessage MULTIPART_DATA_EMTPY = new ExceptionMessage(1510, 400, "Multipart data is empty");
    public static final ExceptionMessage MULTIPART_TYPE_UNDEFINED = new ExceptionMessage(1511, 400, "Multipart data type undefined");
    public static final ExceptionMessage MULTIPART_FILE_NAME_UNDEFINED = new ExceptionMessage(1512, 400, "Multipart file name undefined");
    public static final ExceptionMessage MULTIPART_FILE_TYPE_NOT_COMPATIBLE = new ExceptionMessage(1513, 400, "Multipart file type not compatible");
    public static final ExceptionMessage MAX_UPLOAD_SIZE_EXCEEDED = new ExceptionMessage(1514, 400, "Maximum upload size exceeded");
    public static final ExceptionMessage UPLOAD_FAILED = new ExceptionMessage(1515, 400, "Upload file failed");

    public static final ExceptionMessage USER_FILE_NOT_FOUND = new ExceptionMessage(1516, 400, "User file not found");
    public static final ExceptionMessage CURRENT_USER_DONT_HAVE_ACCESS_TO_FILE = new ExceptionMessage(1517, 401, "User don't have access to file");

}
