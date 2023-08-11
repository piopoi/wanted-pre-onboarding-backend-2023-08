package com.wanted.wantedpreonboardingbackend.post.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wanted.wantedpreonboardingbackend.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostTest {

    @Test
    @DisplayName("글을 생성할 수 있다.")
    void createPost() {
        //given
        String title = "제목";
        String content = "내용";
        Member member = Member.builder()
                .email("test@test.com")
                .password("12345678")
                .build();

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
}
