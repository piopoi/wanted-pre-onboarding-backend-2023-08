package com.wanted.wantedpreonboardingbackend.post.ui;

import com.wanted.wantedpreonboardingbackend.auth.domain.LoginMember;
import com.wanted.wantedpreonboardingbackend.member.domain.Member;
import com.wanted.wantedpreonboardingbackend.post.dto.PostRequest;
import com.wanted.wantedpreonboardingbackend.post.dto.PostResponse;
import com.wanted.wantedpreonboardingbackend.post.service.PostService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<Void> createPost(@AuthenticationPrincipal LoginMember loginMember,
                                           @RequestBody @Valid PostRequest postRequest) {
        Member member = loginMember.getMember();
        PostResponse postResponse = postService.createPost(postRequest, member.getId());
        return ResponseEntity.created(URI.create("/post/" + postResponse.getId())).build();
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> findPosts(@PageableDefault(size = 5, page = 1, sort = "id", direction = Direction.DESC)
                                                        Pageable pageable) {
        List<PostResponse> postResponses = postService.findPosts(pageable);
        return ResponseEntity.ok().body(postResponses);
    }
}
