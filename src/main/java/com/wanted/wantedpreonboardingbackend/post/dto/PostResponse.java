package com.wanted.wantedpreonboardingbackend.post.dto;

import com.wanted.wantedpreonboardingbackend.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private Long memberId;

    public static PostResponse of(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .memberId(post.getMember().getId())
                .build();
    }
}
