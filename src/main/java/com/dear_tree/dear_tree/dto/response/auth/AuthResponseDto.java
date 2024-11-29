package com.dear_tree.dear_tree.dto.response.auth;

import com.dear_tree.dear_tree.common.ResponseCode;
import com.dear_tree.dear_tree.common.ResponseMessage;
import com.dear_tree.dear_tree.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class AuthResponseDto extends ResponseDto {
    private AuthResponseDto() {
        super();
    }

    public static ResponseEntity<AuthResponseDto> success() {
        AuthResponseDto result = new AuthResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> duplicatedUsername() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.DUPLICATE_USERNAME, ResponseMessage.DUPLICATE_USERNAME);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> passwordMismatch() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.PASSWORD_MISMATCH, ResponseMessage.PASSWORD_MISMATCH);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> signInFail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> refreshTokenExpired() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.REFRESH_TOKEN_EXPIRED, ResponseMessage.REFRESH_TOKEN_EXPIRED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> invalidAccessToken() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.INVALID_ACCESS_TOKEN, ResponseMessage.INVALID_ACCESS_TOKEN);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
