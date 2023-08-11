package com.wanted.wantedpreonboardingbackend.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        given(postRepository.save(any())).willReturn(post);
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
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        //when
        PostResponse postResponse = postService.findPost(post.getId());

        //then
        assertThat(postResponse).isNotNull();
        assertThat(postResponse.getId()).isEqualTo(post.getId());
    }

    @Test
    @DisplayName("id로 글을 삭제할 수 있다.")
    void deletePost() {
        //given
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        //when
        postService.deletePost(post.getId(), member.getId());

        //then
        assertThat(postRepository.findAll()).hasSize(0);
    }

    @Test
    @DisplayName("존재하지 않는 글은 삭제할 수 없다.")
    void invalidPostId_deletePost() {
        //given
        Long invalidPostId = 99L;

        //when then
        assertThatThrownBy(() -> postService.deletePost(invalidPostId, member.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 글입니다.");
    }
    @Test
    @DisplayName("작성자가 아니면 글은 삭제할 수 없다.")
    void invalidMemberId_deletePost() {
        //given
        Long invalidMemberId = 2L;
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        //when then
        assertThatThrownBy(() -> postService.deletePost(post.getId(), invalidMemberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("권한이 없습니다.");
    }
}
