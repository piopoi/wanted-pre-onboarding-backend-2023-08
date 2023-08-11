package com.wanted.wantedpreonboardingbackend.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.wanted.wantedpreonboardingbackend.member.domain.Member;
import com.wanted.wantedpreonboardingbackend.member.repository.MemberRepository;
import com.wanted.wantedpreonboardingbackend.post.domain.Post;
import com.wanted.wantedpreonboardingbackend.post.dto.PostRequest;
import com.wanted.wantedpreonboardingbackend.post.dto.PostResponse;
import com.wanted.wantedpreonboardingbackend.post.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private MemberRepository memberRepository;

    private Member member;
    private Post post;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .email("test@test.com")
                .password("12345678")
                .build();
        setField(member, "id", 1L);

        post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();
        setField(post, "id", 1L);
    }

    @Test
    @DisplayName("글을 등록할 수 있다.")
    void createPost() {
        //given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();
        given(postRepository.save(any())).willReturn(post);
        setField(post, "id", 1L);
        PostRequest postRequest = PostRequest.builder()
                .title("제목")
                .content("내용")
                .build();

        //when
        PostResponse postResponse = postService.createPost(postRequest, member.getId());

        //then
        assertThat(postResponse).isNotNull();
        assertThat(postResponse.getTitle()).isEqualTo(postRequest.getTitle());
        assertThat(postResponse.getContent()).isEqualTo(postRequest.getContent());
        assertThat(postResponse.getMemberId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("글 목록을 조회할 수 있다.")
    void findPosts() {
        //given
        Pageable pageable = Pageable.ofSize(5);
        List<Post> posts = IntStream.range(1, 6)
                .mapToObj(i -> {
                    Post post = Post.builder()
                            .title("제목" + i)
                            .content("내용" + i)
                            .member(member)
                            .build();
                    setField(post, "id", (long) i);
                    return post;
                })
                .toList();
        given(postRepository.findAll(pageable)).willReturn(new PageImpl<>(posts));

        //when
        List<PostResponse> postResponses = postService.findPosts(pageable);

        //then
        assertThat(postResponses).hasSize(posts.size());
    }

    @Test
    @DisplayName("id로 글을 조회할 수 있다.")
    void findPost() {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();
        setField(post, "id", 1L);
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        //when
        PostResponse postResponse = postService.findPost(post.getId());

        //then
        assertThat(postResponse).isNotNull();
        assertThat(postResponse.getId()).isEqualTo(post.getId());
    }
}
