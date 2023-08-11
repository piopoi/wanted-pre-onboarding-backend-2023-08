package com.wanted.wantedpreonboardingbackend.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    private final String EMAIL = "test@abc.com";
    private final String PASSWORD = "12345678";
    private Member member;

    @BeforeEach
    void beforeEach() {
        member = new Member(EMAIL, PASSWORD);
    }

    @Test
    @DisplayName("사용자를 생성할 수 있다.")
    void createMember() {
        //then
        assertThat(member).isNotNull();
        assertThat(member.getEmail()).isEqualTo(EMAIL);
    }

    @Test
    @DisplayName("이메일에 '@'가 없는 경우, 사용자를 생성할 수 없다.")
    void invalidEmail() {
        //given
        String invalidEmail = "test_abc.com";

        //expected
        assertThatThrownBy(() -> new Member(invalidEmail, PASSWORD))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일 주소 형식에 맞게 입력해주세요.");
    }

    @Test
    @DisplayName("비밀번호는 8자 이상인 경우, 사용자를 생성할 수 없다.")
    void inavlidPassword() {
        //given
        String invalidPassword = "1234567";

        //expected
        assertThatThrownBy(() -> new Member(EMAIL, invalidPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("8자 이상의 비밀번호를 사용해주세요.");
    }
}
