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
    String REFRESH_TOKEN_EXPIRED = "refresh token is expired";
    String INVALID_ACCESS_TOKEN = "invalid access token";

    // HTTP Status 500
    String DATABASE_ERROR = "database error";
    String INTERNAL_SERVER_ERROR = "unexpected error occurred";
}
