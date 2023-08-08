package com.wanted.wantedpreonboardingbackend.member.dto;

import com.wanted.wantedpreonboardingbackend.member.domain.Member;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberRequest {

    @NotNull(message = "이메일을 입력해주세요.")
    private String email;

    @NotNull(message = "비밀번호를 입력해주세요.")
    private String password;

    public Member toMember() {
        return Member.builder()
                .email(email)
                .password(password)
                .build();
    }
}
