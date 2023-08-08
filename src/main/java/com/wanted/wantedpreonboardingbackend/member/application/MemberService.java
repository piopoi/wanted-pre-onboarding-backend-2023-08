package com.wanted.wantedpreonboardingbackend.member.application;

import com.wanted.wantedpreonboardingbackend.member.domain.Member;
import com.wanted.wantedpreonboardingbackend.member.dto.MemberRequest;
import com.wanted.wantedpreonboardingbackend.member.dto.MemberResponse;
import com.wanted.wantedpreonboardingbackend.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRequest.toMember();
        member.encodePassword(passwordEncoder);
        Member savedMember = memberRepository.save(member);
        return MemberResponse.of(savedMember);
    }
}
