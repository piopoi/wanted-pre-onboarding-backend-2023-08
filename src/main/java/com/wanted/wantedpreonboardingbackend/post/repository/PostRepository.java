package com.wanted.wantedpreonboardingbackend.post.repository;

import com.wanted.wantedpreonboardingbackend.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
