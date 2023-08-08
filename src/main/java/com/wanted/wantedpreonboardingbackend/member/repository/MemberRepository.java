package com.wanted.wantedpreonboardingbackend.member.repository;

import com.wanted.wantedpreonboardingbackend.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
}
