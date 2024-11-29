package com.dear_tree.dear_tree.common;

public interface ResponseCode {
    //HTTP Status 200
    String SUCCESS = "SU";

    // HTTP Status 400
    String DUPLICATE_USERNAME = "DU";
    String VALIDATION_ERROR = "VE";
    String PASSWORD_MISMATCH = "PM";
    String NOT_EXIST_USER = "NEU";

    //HTTP Status 401
    String AUTHORIZATION_FAIL = "AF";
    String SIGN_IN_FAIL = "SF";
    String REFRESH_TOKEN_EXPIRED = "RTE";
    String INVALID_ACCESS_TOKEN = "IAT";

    // HTTP Status 500
    String DATABASE_ERROR = "DBE";
    String INTERNAL_SERVER_ERROR = "ISE";
}
