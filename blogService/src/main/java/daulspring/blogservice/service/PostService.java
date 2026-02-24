package daulspring.blogservice.service;

import daulspring.blogservice.dto.PostCreateRequestDTO;
import daulspring.blogservice.dto.PostResponseDTO;

public interface PostService {

  // 게시글 작성
  PostResponseDTO createPost(Long userId, PostCreateRequestDTO dto);
  
  // 게시글 조회
  PostResponseDTO getPostById(Long postId);
}