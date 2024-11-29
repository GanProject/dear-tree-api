package com.dear_tree.dear_tree.dto.response;

import com.dear_tree.dear_tree.common.ResponseCode;
import com.dear_tree.dear_tree.common.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class ResponseDto {

    private int status;
    private String message;

    public ResponseDto() {
        this.status = HttpStatus.OK.value();
        this.message = ResponseMessage.SUCCESS;
    }

    public static ResponseEntity<ResponseDto> noAuthentication() {
        ResponseDto result = new ResponseDto(HttpStatus.UNAUTHORIZED.value(), ResponseMessage.AUTHORIZATION_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    public static ResponseEntity<ResponseDto> databaseError() {
        ResponseDto responseBody = new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> validationError(String message) {
        ResponseDto responseBody = new ResponseDto(HttpStatus.BAD_REQUEST.value(), message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> internalServerError() {
        ResponseDto responseBody = new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseMessage.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notExistUser() {
        ResponseDto responseBody = new ResponseDto(HttpStatus.UNAUTHORIZED.value(), ResponseMessage.NOT_EXIST_USER);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
