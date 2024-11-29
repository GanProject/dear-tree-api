package com.dear_tree.dear_tree.controller;

import com.dear_tree.dear_tree.dto.request.RefreshAccessTokenDto;
import com.dear_tree.dear_tree.dto.request.SignInRequestDto;
import com.dear_tree.dear_tree.dto.request.SignUpRequestDto;
import com.dear_tree.dear_tree.dto.response.ResponseDto;
import com.dear_tree.dear_tree.dto.response.auth.AuthResponseDto;
import com.dear_tree.dear_tree.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<? super AuthResponseDto> signUp(@RequestBody @Valid SignUpRequestDto requestBody) {
        ResponseEntity<? super AuthResponseDto> response = authService.signUp(requestBody);
        return response;
    }

    @GetMapping("/check-username")
    @Operation(summary = "닉네임 중복 확인", description = "닉네임이 중복되는지 확인.")
    @ApiResponse(responseCode = "200", description = "성공(중복되지 않음)")
    @ApiResponse(responseCode = "400", description = "이미 존재하는 닉네임")
    @ApiResponse(responseCode = "500", description = "DB 접근 오류")
    public ResponseEntity<? super AuthResponseDto> checkUsername(@RequestParam(value = "username") String username) {
        ResponseEntity<? super AuthResponseDto> response = authService.checkUsername(username);
        return response;
    }

    @PostMapping("/sign-in")
    @Operation(summary = "로그인", description = "로그인합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호가 일치하지 않음")
    @ApiResponse(responseCode = "500", description = "DB 접근 오류")
    public ResponseEntity<? super AuthResponseDto> signIn(@RequestBody @Valid SignInRequestDto requestBody, HttpServletResponse httpServletResponse) {
        ResponseEntity<? super AuthResponseDto> response = authService.signIn(requestBody, httpServletResponse);
        return response;
    }

    @PostMapping("/access-token/refresh")
    @Operation(summary = "액세스토큰 재발급", description = "액세스토큰 만료시 재발급합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "401", description = "리프레시토큰 만료, 재로그인 필요")
    @ApiResponse(responseCode = "500", description = "DB 접근 오류")
    public ResponseEntity<? super AuthResponseDto> refreshAccessToken(@RequestBody RefreshAccessTokenDto requestBody, HttpServletResponse httpServletResponse) {
        ResponseEntity<? super AuthResponseDto> response = authService.refreshAccessToken(requestBody, httpServletResponse);
        return response;
    }

    @PostMapping("/sign-out")
    @Operation(summary = "로그아웃", description = "로그아웃(액세스토큰 블랙리스트 올리기, 리프레시토큰 삭제)")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "401", description = "response code : IAT, 유효하지 않은 액세스토큰 / response code : AF, 사용자 인증 실패")
    @ApiResponse(responseCode = "500", description = "DB 접근 오류")
    public ResponseEntity<? super AuthResponseDto> signOut(HttpServletRequest request) {
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

        ResponseEntity<? super AuthResponseDto> response = authService.signOut(username, request);
        return response;
    }

    @DeleteMapping("/delete")
    @Operation(summary = "회원탈퇴", description = "회원탈퇴합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "존재하지 않는 회원")
    @ApiResponse(responseCode = "401", description = "response code : IAT, 유효하지 않은 액세스토큰 / response code : AF, 사용자 인증 실패")
    @ApiResponse(responseCode = "500", description = "DB 접근 오류 / 서버 오류")
    public ResponseEntity<? super AuthResponseDto> delete(HttpServletRequest request) {
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

        ResponseEntity<? super AuthResponseDto> response = authService.delete(username, request);
        return response;
    }
}
