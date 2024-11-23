package com.dear_tree.dear_tree.service;

import com.dear_tree.dear_tree.domain.Member;
import com.dear_tree.dear_tree.dto.request.SignInRequestDto;
import com.dear_tree.dear_tree.dto.request.SignUpRequestDto;
import com.dear_tree.dear_tree.dto.response.ResponseDto;
import com.dear_tree.dear_tree.repository.MemberRepository;
import com.dear_tree.dear_tree.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public ResponseEntity<ResponseDto> signUp(SignUpRequestDto dto) {
        try {
            if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
                return ResponseDto.passwordMismatch();
            }

            String hash = new BCryptPasswordEncoder().encode(dto.getPassword());
            dto.setPassword(hash);
            Member member = new Member(dto);
            memberRepository.save(member);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto());
    }

    public ResponseEntity<ResponseDto> checkUsername(String username) {
        try {
            Member member = memberRepository.findByUsernameAndStatus(username, true);

            if (member != null) {
                return ResponseDto.duplicatedUsername();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto());
    }

    public ResponseEntity<ResponseDto> signIn(SignInRequestDto dto, HttpServletResponse httpServletResponse) {

        String accessToken;
        String refreshToken;

        try {
            //아이디 불일치
            String username = dto.getUsername();
            Member member = memberRepository.findByUsernameAndStatus(username, true);
            if (member == null || member.getStatus() == false)
                return ResponseDto.signInFail();

            //비밀번호 불일치
            String password = dto.getPassword();
            String encodedPassword = member.getPassword();
            boolean isMatched = new BCryptPasswordEncoder().matches(password, encodedPassword);
            if (!isMatched)
                return ResponseDto.signInFail();

            accessToken = JwtUtil.createToken(username, 1000 * 60 * 30L);
            refreshToken = JwtUtil.createToken(username, 1000 * 60 * 60 * 24 * 30L);

            //쿠키에 액세스토큰 저장
            Cookie cookie = new Cookie("accessToken", accessToken);
            cookie.setHttpOnly(true);
            //cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge((int) (1000 * 60 * 30L / 1000));

            httpServletResponse.addCookie(cookie);

            //레디스에 리프레시토큰 저장
            long expiredTime = 1000L * 60 * 60 * 24 * 30;
            String redisKey = "RefreshToken:" + username;
            redisTemplate.opsForValue().set(redisKey, refreshToken, expiredTime, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto());

    }
}
