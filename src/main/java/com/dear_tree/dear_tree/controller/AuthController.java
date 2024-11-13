package com.dear_tree.dear_tree.controller;

import com.dear_tree.dear_tree.dto.request.SignUpRequestDto;
import com.dear_tree.dear_tree.dto.response.ResponseDto;
import com.dear_tree.dear_tree.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Auth API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("sign-up")
    @Operation(summary = "회원가입", description = "회원을 추가합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "500", description = "DB 접근 오류")
    public ResponseEntity<ResponseDto> signUp(@RequestBody @Valid SignUpRequestDto requestBody) {
        ResponseEntity<ResponseDto> response = authService.signUp(requestBody);
        return response;
    }
}
