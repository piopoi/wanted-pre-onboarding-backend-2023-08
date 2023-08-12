package com.wanted.wantedpreonboardingbackend.post.dto;

import static com.wanted.wantedpreonboardingbackend.post.constants.PostConstants.POST_EMPTY_CONTENT;
import static com.wanted.wantedpreonboardingbackend.post.constants.PostConstants.POST_EMPTY_TITLE;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PostRequest {

    @NotBlank(message = POST_EMPTY_TITLE)
    private String title;

    @NotBlank(message = POST_EMPTY_CONTENT)
    private String content;
}
