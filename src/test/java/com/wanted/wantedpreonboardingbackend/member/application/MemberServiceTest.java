package com.wanted.wantedpreonboardingbackend.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import com.wanted.wantedpreonboardingbackend.member.domain.Member;
import com.wanted.wantedpreonboardingbackend.member.dto.MemberRequest;
import com.wanted.wantedpreonboardingbackend.member.dto.MemberResponse;
import com.wanted.wantedpreonboardingbackend.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private MemberRequest memberRequest;
    private Member member;

    @BeforeEach
    void setUp() {
        //given
        memberRequest = MemberRequest.builder()
                .email("test@email.com")
                .password("12345678")
                .build();
        member = memberRequest.toMember();
        ReflectionTestUtils.setField(member, "id", 1L);
    }

    @Test
    @DisplayName("사용자를 생성할 수 있다")
    void createMember() {
        //given
        given(memberRepository.save(any())).willReturn(member);

        //when
        MemberResponse createdMember = memberService.createMember(memberRequest);

        //then
        assertThat(createdMember.getId()).isEqualTo(member.getId());
        assertThat(createdMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("잘못된 이메일로 사용자를 생성할 수 없다")
    void invalidEmail_createMember() {
        //given
        ReflectionTestUtils.setField(memberRequest, "email", "test1234");

        //when then
        assertThatThrownBy(() -> memberService.createMember(memberRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일 주소 형식에 맞게 입력해주세요.");
    }

    @Test
    @DisplayName("이메일 없이 사용자를 생성할 수 없다")
    void nullEmail_createMember() {
        //given
        ReflectionTestUtils.setField(memberRequest, "email", null);

        //when then
        assertThatThrownBy(() -> memberService.createMember(memberRequest))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("잘못된 비밀번호로 사용자를 생성할 수 없다")
    void invalidPassword_createMember() {
        //given
        ReflectionTestUtils.setField(memberRequest, "password", "123");

        //when then
        assertThatThrownBy(() -> memberService.createMember(memberRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("8자 이상의 비밀번호를 사용해주세요.");
    }

    @Test
    @DisplayName("비밀번호 없이 사용자를 생성할 수 없다")
    void nullPassword_createMember() {
        //given
        ReflectionTestUtils.setField(memberRequest, "password", null);

        //when then
        assertThatThrownBy(() -> memberService.createMember(memberRequest))
                .isInstanceOf(NullPointerException.class);
    }
}
