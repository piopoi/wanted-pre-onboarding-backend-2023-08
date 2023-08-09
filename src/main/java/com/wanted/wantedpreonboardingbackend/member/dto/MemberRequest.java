package com.wanted.wantedpreonboardingbackend.member.dto;

import com.wanted.wantedpreonboardingbackend.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "8자 이상의 비밀번호를 사용해주세요.")
    private String password;

    @Builder
    public MemberRequest(String email, String password) {
        validateEmail(email);

        this.email = email;
        this.password = password;
    }

    private void validateEmail(String email) {
        if (!email.contains("@")) {
            throw new IllegalArgumentException("이메일 주소 형식에 맞게 입력해주세요.");
        }
    }

    public Member toMember() {
        return Member.builder()
                .email(email)
                .password(password)
                .build();
    }
}
