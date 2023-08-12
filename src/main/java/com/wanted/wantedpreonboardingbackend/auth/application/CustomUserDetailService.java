package com.wanted.wantedpreonboardingbackend.auth.application;

import com.wanted.wantedpreonboardingbackend.auth.AuthConstants;
import com.wanted.wantedpreonboardingbackend.auth.domain.LoginMember;
import com.wanted.wantedpreonboardingbackend.member.domain.Member;
import com.wanted.wantedpreonboardingbackend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(AuthConstants.AUTH_MEMBER_NOT_EXISTS));
        return new LoginMember(member);
    }
}
