package com.dear_tree.dear_tree.common;

public interface ResponseMessage {
    // HTTP Status 200
    String SUCCESS = "success";

    // HTTP Status 400
    String DUPLICATE_USERNAME = "duplicate username";
    String VALIDATION_ERROR = "validation error";
    String PASSWORD_MISMATCH = "password and password confirmation do not match";

    // HTTP Status 401
    String AUTHORIZATION_FAIL = "authorization failed";
    String SIGN_IN_FAIL = "invalid nickname or password";

    // HTTP Status 500
    String DATABASE_ERROR = "database error";
}
