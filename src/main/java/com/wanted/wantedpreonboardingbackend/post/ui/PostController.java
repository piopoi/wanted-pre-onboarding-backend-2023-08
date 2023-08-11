package com.wanted.wantedpreonboardingbackend.post.ui;

import com.wanted.wantedpreonboardingbackend.auth.domain.LoginMember;
import com.wanted.wantedpreonboardingbackend.post.dto.PostRequest;
import com.wanted.wantedpreonboardingbackend.post.dto.PostResponse;
import com.wanted.wantedpreonboardingbackend.post.service.PostService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<Void> createPost(@AuthenticationPrincipal LoginMember loginMember,
                                           @RequestBody @Valid PostRequest postRequest) {
        PostResponse postResponse = postService.createPost(postRequest, loginMember.getMember().getId());
        return ResponseEntity.created(URI.create("/post/" + postResponse.getId())).build();
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> findPosts(@PageableDefault(size = 5)
                                                        Pageable pageable) {
        List<PostResponse> postResponses = postService.findPosts(pageable);
        return ResponseEntity.ok().body(postResponses);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<PostResponse> findPost(@PathVariable Long id) {
        PostResponse postResponse = postService.findPost(id);
        return ResponseEntity.ok().body(postResponse);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<Void> updatePost(@AuthenticationPrincipal LoginMember loginMember,
                                           @PathVariable Long id,
                                           @RequestBody @Valid PostRequest postRequest) {
        postService.updatePost(id, loginMember.getMember().getId(), postRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal LoginMember loginMember,
                                           @PathVariable Long id) {
        postService.deletePost(id, loginMember.getMember().getId());
        return ResponseEntity.noContent().build();
    }
}
