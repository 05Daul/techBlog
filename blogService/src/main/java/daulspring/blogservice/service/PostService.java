package daulspring.blogservice.service;

import daulspring.blogservice.dto.PostCreateRequestDTO;
import daulspring.blogservice.dto.PostResponseDTO;
import daulspring.blogservice.dto.PostUpdateRequestDTO;
import daulspring.blogservice.entity.PostEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

  // 게시글 CRUD
  PostResponseDTO createPost(Long userId, PostCreateRequestDTO dto);
  PostResponseDTO updatePost(Long userId, Long postId, PostUpdateRequestDTO dto);
  void deletePost(Long userId, Long postId);
  PostResponseDTO getPostById(Long postId);
  
  // 목록 조회
  Page<PostResponseDTO> getRecentPosts(Pageable pageable);
  Page<PostResponseDTO> getTrendingPosts(Pageable pageable);
  Page<PostResponseDTO> getFeedPosts(Long userId, Pageable pageable);
  Page<PostResponseDTO> getMyPosts(Long userId, Pageable pageable);
  Page<PostResponseDTO> searchPosts(String keyword, Pageable pageable);
  
  // 태그 관리
  List<String> getTagsByPostId(Long postId);
  void addTagToPost(Long postId, String tagName);
  void addTagsToPost(Long postId, List<String> tagNames);
  void removeTagFromPost(Long postId, String tagName);
  
  // 조회수
  void incrementViewCount(Long postId);
  
  // 이미지 업로드
  String uploadImage(MultipartFile file);
}