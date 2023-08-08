package com.wanted.wantedpreonboardingbackend.member.dto;

import com.wanted.wantedpreonboardingbackend.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class MemberResponse {

    private Long id;

    private String email;

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .build();
    }
}
