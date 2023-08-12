package com.wanted.wantedpreonboardingbackend.member.dto;

import static com.wanted.wantedpreonboardingbackend.member.constants.MemberConstants.*;

import com.wanted.wantedpreonboardingbackend.member.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberRequest {

    @NotBlank(message = MEMBER_EMAIL_EMPTY)
    @Email(message = MEMBER_EMAIL_INVALID)
    private final String email;

    @NotBlank(message = MEMBER_PASSWORD_EMPTY)
    @Size(min = 8, message = MEMBER_PASSWORD_LENGTH_MIN)
    private final String password;

    @Builder
    public MemberRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member toMember() {
        return Member.builder()
                .email(email)
                .password(password)
                .build();
    }
}
