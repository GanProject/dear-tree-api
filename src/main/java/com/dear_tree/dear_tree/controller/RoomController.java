package com.dear_tree.dear_tree.controller;

import com.dear_tree.dear_tree.dto.request.auth.RefreshAccessTokenDto;
import com.dear_tree.dear_tree.dto.request.room.CreateRoomRequestDto;
import com.dear_tree.dear_tree.dto.response.ResponseDto;
import com.dear_tree.dear_tree.dto.response.auth.AuthResponseDto;
import com.dear_tree.dear_tree.dto.response.room.RoomResponseDto;
import com.dear_tree.dear_tree.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Room", description = "Room API")
@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("")
    @Operation(summary = "방 생성", description = "방을 생성합니다. icon은 SOCKS, WREATH, SNOWMAN, GIFT 중 하나로 입력")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "존재하지 않는 회원/잘못된 아이콘 Enum값")
    @ApiResponse(responseCode = "401", description = "response code : IAT, 유효하지 않은 액세스토큰 / response code : AF, 사용자 인증 실패")
    @ApiResponse(responseCode = "500", description = "DB 접근 오류 / 서버 오류")
    public ResponseEntity<? super RoomResponseDto> createRoom(@RequestBody CreateRoomRequestDto requestBody) {
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("auth : " + authentication);
        try {
            if (authentication != null) {
                username = authentication.getName();
            }
            if (username == null)
                return ResponseDto.noAuthentication();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.internalServerError();
        }

        ResponseEntity<? super RoomResponseDto> response = roomService.createRoom(username, requestBody);
        return response;
    }

}
