package com.dear_tree.dear_tree.common;

public interface ResponseCode {
    //HTTP Status 200
    String SUCCESS = "SU";

    // HTTP Status 400
    String DUPLICATE_USERNAME = "DU";
    String VALIDATION_ERROR = "VE";
    String PASSWORD_MISMATCH = "PM";

    //HTTP Status 401
    String AUTHORIZATION_FAIL = "AF";

    // HTTP Status 500
    String DATABASE_ERROR = "DBE";
}
