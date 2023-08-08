package com.wanted.wantedpreonboardingbackend.member.ui;

import com.wanted.wantedpreonboardingbackend.member.application.MemberService;
import com.wanted.wantedpreonboardingbackend.member.dto.MemberRequest;
import com.wanted.wantedpreonboardingbackend.member.dto.MemberResponse;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member")
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberRequest memberRequest) {
        MemberResponse memberResponse = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/member/" + memberResponse.getId())).build();
    }
}
