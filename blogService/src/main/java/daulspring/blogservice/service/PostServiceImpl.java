package daulspring.blogservice.service;

import daulspring.blogservice.dto.PostCreateRequestDTO;
import daulspring.blogservice.dto.PostResponseDTO;
import daulspring.blogservice.entity.PostEntity;
import daulspring.blogservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;

  @Override
  public PostResponseDTO createPost(Long userId, PostCreateRequestDTO dto) {
    PostEntity postEntity = new PostEntity();
    postEntity.setUserId(userId);
    postEntity.setTitle(dto.getTitle());
    postEntity.setContent(dto.getContent());
    postEntity.setThumbnail(dto.getThumbnail());
    postEntity.setIsPublished(dto.getIsPublished());
    postEntity.setSeriesId(dto.getSeriesId());
    postEntity.setIsCrawled(false);

    PostEntity savedPost = postRepository.save(postEntity);
    log.info("게시글 작성 완료: postId={}, userId={}", savedPost.getPostId(), userId);

    return convertToResponseDTO(postEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public PostResponseDTO getPostById(Long postId) {
    PostEntity post = postRepository.findByPostId(postId)
        .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

    return convertToResponseDTO(post);
  }

  private PostResponseDTO convertToResponseDTO(PostEntity post) {
    return PostResponseDTO.builder()
        .postId(post.getPostId())
        .userId(post.getUserId())
        .seriesId(post.getSeriesId())
        .title(post.getTitle())
        .content(post.getContent())
        .thumbnail(post.getThumbnail())
        .isPublished(post.getIsPublished())
        .isCrawled(post.getIsCrawled())
        .originUrl(post.getOriginUrl())
        .viewCount(post.getViewCount())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .build();
  }
}
