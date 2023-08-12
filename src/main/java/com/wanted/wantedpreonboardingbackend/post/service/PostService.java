package com.wanted.wantedpreonboardingbackend.post.service;

import static com.wanted.wantedpreonboardingbackend.member.constants.MemberConstants.MEMBER_NOT_EXISTS;
import static com.wanted.wantedpreonboardingbackend.post.constants.PostConstants.POST_NOT_EXISTS;
import static com.wanted.wantedpreonboardingbackend.post.constants.PostConstants.POST_NO_AUTH;

import com.wanted.wantedpreonboardingbackend.member.domain.Member;
import com.wanted.wantedpreonboardingbackend.member.repository.MemberRepository;
import com.wanted.wantedpreonboardingbackend.post.domain.Post;
import com.wanted.wantedpreonboardingbackend.post.dto.PostRequest;
import com.wanted.wantedpreonboardingbackend.post.dto.PostResponse;
import com.wanted.wantedpreonboardingbackend.post.repository.PostRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostResponse createPost(PostRequest postRequest, Long memberId) {
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .member(findMemberById(memberId))
                .build();
        Post createdPost = postRepository.save(post);
        return PostResponse.of(createdPost);
    }

    public List<PostResponse> findPosts(Pageable pageable) {
        List<Post> posts = postRepository.findAll(pageable).getContent();
        return posts.stream()
                .map(PostResponse::of)
                .toList();
    }

    public PostResponse findPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXISTS));
        return PostResponse.of(post);
    }

    public PostResponse updatePost(Long postId, Long memberId, PostRequest postRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXISTS));
        validatePostRegister(post.getMember().getId(), memberId);
        post.update(postRequest.getTitle(), postRequest.getContent());
        return PostResponse.of(post);
    }

    public void deletePost(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXISTS));
        validatePostRegister(post.getMember().getId(), memberId);
        postRepository.deleteById(postId);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(MEMBER_NOT_EXISTS));
    }

    private void validatePostRegister(Long postMemberId, Long memberId) {
        if(!Objects.equals(postMemberId, memberId)) {
            throw new IllegalArgumentException(POST_NO_AUTH);
        }
    }
}
