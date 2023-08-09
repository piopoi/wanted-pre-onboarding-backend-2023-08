package com.wanted.wantedpreonboardingbackend.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {

    USER("ROLE_USER");

    private final String value;
}
