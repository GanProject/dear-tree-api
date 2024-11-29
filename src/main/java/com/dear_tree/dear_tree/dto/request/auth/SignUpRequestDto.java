package com.dear_tree.dear_tree.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDto {

    @NotEmpty(message = "닉네임은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "닉네임은 특수문자를 제외한 2자 이상, 10자 이하여야 합니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp="^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>]{8,15}$", message = "비밀번호는 영문, 숫자를 포함해 8자 이상, 15자 이하여야 합니다.")
    private String password;

    @NotEmpty(message = "비밀번호 확인은 필수 입력값입니다.")
    private String passwordConfirm;
}
