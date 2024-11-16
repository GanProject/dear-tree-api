package com.dear_tree.dear_tree.service;

import com.dear_tree.dear_tree.domain.Member;
import com.dear_tree.dear_tree.dto.request.SignUpRequestDto;
import com.dear_tree.dear_tree.dto.response.ResponseDto;
import com.dear_tree.dear_tree.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ResponseEntity<ResponseDto> signUp(SignUpRequestDto dto) {
        try {
            String hash = passwordEncoder.encode(dto.getPassword());
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
}
