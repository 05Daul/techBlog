package daulspring.blogservice.service;

import daulspring.blogservice.dto.PostCreateRequestDTO;
import daulspring.blogservice.dto.PostResponseDTO;
import daulspring.blogservice.dto.PostUpdateRequestDTO;
import daulspring.blogservice.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

  // 게시글 작성
  PostResponseDTO createPost(Long userId, PostCreateRequestDTO dto);
  
  // 게시글 조회
  PostResponseDTO getPostById(Long postId);

  // 게시물 업데이트
  PostResponseDTO updatePost(Long userId, Long postId, PostUpdateRequestDTO dto);

  // 게시글 삭제
  void deletePost(Long userId, Long postId);


  // 최신순 목록 조회
  Page<PostResponseDTO> getRecentPosts(Pageable pageable);

  // 내 게시글 목록
  Page<PostResponseDTO> getMyPosts(Long userId, Pageable pageable);

  void incrementViewCount(Long postId);

}