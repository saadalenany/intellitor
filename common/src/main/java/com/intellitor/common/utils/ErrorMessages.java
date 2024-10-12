package com.intellitor.common.utils;

public final class ErrorMessages {

    private ErrorMessages() {
        throw new IllegalStateException("ErrorMessages class");
    }

    /**
     * 1st %s param: Object/Table
     * 2nd %s param: ID value
     **/
    public static final String NO_OBJECT_FOUND_BY_ID = "No %s found with ID [%s]";

    /**
     * 1st %s param: Object/Table
     * 2nd %s param: Email value
     * 3rd %s param: Password value
     **/
    public static final String NO_OBJECT_FOUND_BY_EMAIL_PASSWORD = "No %s found with Email [%s] & Password [%s]";

    /**
     * 1st %s param: Object/Table
     * 2nd %s param: Reason for error
     **/
    public static final String UNABLE_CREATE_OBJECT_DUE_REASON = "Unable to create [%s] due to [%s]";

    /**
     * 1st %s param: Email value
     * 2nd %s param: Password value
     **/
    public static final String USER_ALREADY_EXISTS_BY_EMAIL_PASSWORD = "User with Email [%s] & Password [%s] already exists";

    /**
     * 1st %s param: Class/Table
     * 2nd %s param: unique field/column name
     * 3rd %s param: unique field/column value
     **/
    public static final String OBJECT_FIELD_ALREADY_EXISTS = "%s %s by value [%s] already exists";

}
