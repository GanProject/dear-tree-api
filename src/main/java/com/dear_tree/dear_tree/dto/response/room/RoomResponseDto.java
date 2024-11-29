package com.dear_tree.dear_tree.dto.response.room;

import com.dear_tree.dear_tree.dto.response.ResponseDto;
import com.dear_tree.dear_tree.dto.response.auth.AuthResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class RoomResponseDto extends ResponseDto {

    private RoomResponseDto() {
        super();
    }

    public static ResponseEntity<RoomResponseDto> success() {
        RoomResponseDto result = new RoomResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
