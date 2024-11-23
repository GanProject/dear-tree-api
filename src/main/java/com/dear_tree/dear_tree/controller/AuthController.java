package com.dear_tree.dear_tree.controller;

import com.dear_tree.dear_tree.dto.request.SignInRequestDto;
import com.dear_tree.dear_tree.dto.request.SignUpRequestDto;
import com.dear_tree.dear_tree.dto.response.ResponseDto;
import com.dear_tree.dear_tree.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "Auth API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "회원을 추가합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "닉네임, 비밀번호 조건 맞지 않음")
    @ApiResponse(responseCode = "400", description = "비밀번호, 비밀번호 확인 일치하지 않음")
    @ApiResponse(responseCode = "500", description = "DB 접근 오류")
    public ResponseEntity<ResponseDto> signUp(@RequestBody @Valid SignUpRequestDto requestBody) {
        ResponseEntity<ResponseDto> response = authService.signUp(requestBody);
        return response;
    }

    @GetMapping("/check-username")
    @Operation(summary = "닉네임 중복 확인", description = "닉네임이 중복되는지 확인.")
    @ApiResponse(responseCode = "200", description = "성공(중복되지 않음)")
    @ApiResponse(responseCode = "400", description = "이미 존재하는 닉네임")
    @ApiResponse(responseCode = "500", description = "DB 접근 오류")
    public ResponseEntity<ResponseDto> checkUsername(@RequestParam(value = "username") String username) {
        ResponseEntity<ResponseDto> response = authService.checkUsername(username);
        return response;
    }

    @PostMapping("/sign-in")
    @Operation(summary = "로그인", description = "로그인합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호가 일치하지 않음")
    @ApiResponse(responseCode = "500", description = "DB 접근 오류")
    public ResponseEntity<ResponseDto> signIn(@RequestBody @Valid SignInRequestDto requestBody, HttpServletResponse httpServletResponse) {
        ResponseEntity<ResponseDto> response = authService.signIn(requestBody, httpServletResponse);
        return response;
    }
}
