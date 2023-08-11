package com.wanted.wantedpreonboardingbackend.post.service;

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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
        return PostResponse.of(post);
    }

    public void deletePost(Long postId, Long memberId) {
        validatePostRegister(postId, memberId);
        postRepository.deleteById(postId);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    private void validatePostRegister(Long postId, Long memberId) {
        Long postMemberId = findPost(postId).getMemberId();
        if(!Objects.equals(postMemberId, memberId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}
