package com.wanted.wantedpreonboardingbackend.post.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wanted.wantedpreonboardingbackend.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostTest {

    private final String title = "제목";
    private final String content = "내용";

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .email("test@test.com")
                .password("12345678")
                .build();
    }

    @Test
    @DisplayName("글을 생성할 수 있다.")
    void createPost() {
        //when
        Post post = Post.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();

        //then
        assertThat(post).isNotNull();
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getMember()).isSameAs(member);
    }

    @Test
    @DisplayName("글을 수정할 수 있다.")
    void updatePost() {
        //given
        Post post = Post.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
        String updatedTitle = "수정된 제목";
        String updatedContent = "수정된 내용";

        //when
        post.update(updatedTitle, updatedContent);

        assertThat(post.getTitle()).isEqualTo(updatedTitle);
        assertThat(post.getContent()).isEqualTo(updatedContent);
    }
}
