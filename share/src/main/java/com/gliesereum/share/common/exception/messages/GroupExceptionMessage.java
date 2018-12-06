package com.gliesereum.share.common.exception.messages;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 05/12/2018
 */
public class GroupExceptionMessage {

    public static final ExceptionMessage GROUP_NOT_FOUND = new ExceptionMessage(1300, 400, "Group not found by id");
    public static final ExceptionMessage USER_EXIST_IN_GROUP = new ExceptionMessage(1301, 400, "User exist in group, remove before add to other");
    public static final ExceptionMessage USER_NOT_EXIST_IN_GROUP = new ExceptionMessage(1302, 400, "User not exist in group");
}
