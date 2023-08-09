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

    public static PostResponse of(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .build();
    }
}
