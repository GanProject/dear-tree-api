package com.dear_tree.dear_tree.dto.request.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignInRequestDto {

    @NotEmpty(message = "닉네임은 필수 입력값입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
