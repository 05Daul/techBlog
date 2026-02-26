package daulspring.blogservice.service;

import daulspring.blogservice.dto.PostCreateRequestDTO;
import daulspring.blogservice.dto.PostResponseDTO;
import daulspring.blogservice.dto.PostUpdateRequestDTO;
import daulspring.blogservice.entity.PostEntity;
import daulspring.blogservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final ViewCountService viewCountService;
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

  @Override
  public void deletePost(Long userId, Long postId) {
    // 1. 게시글 조회
    PostEntity post = postRepository.findByPostId(postId)
        .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

    // 2. 작성자 확인 (본인만 삭제 가능)
    if (!post.getUserId().equals(userId)) {
      throw new IllegalStateException("게시글 삭제 권한이 없습니다.");
    }

    // 3. 삭제
    postRepository.delete(post);

    log.info("게시글 삭제 완료: postId={}, userId={}", postId, userId);

  }

  @Override
  @Transactional
  public PostResponseDTO updatePost(Long userId, Long postId, PostUpdateRequestDTO dto) {

    // 1. 게시글 조회
    PostEntity post = postRepository.findByPostId(postId)
        .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

    // 2. 작성자 확인 (본인만 수정 가능)
    if (!post.getUserId().equals(userId)) {
      throw new IllegalStateException("게시글 수정 권한이 없습니다.");
    }

    // 3. 내용 수정
    post.setTitle(dto.getTitle());
    post.setContent(dto.getContent());
    post.setThumbnail(dto.getThumbnail());

    if (dto.getIsPublished() != null) {
      post.setIsPublished(dto.getIsPublished());
    }

    post.setSeriesId(dto.getSeriesId());

    // 4. 저장
    log.info("게시글 수정 완료: postId={}, userId={}", postId, userId);

    return convertToResponseDTO(post);
  }


  @Override
  public Page<PostResponseDTO> getMyPosts(Long userId, Pageable pageable) {
    log.debug("내 게시글 조회: userId={}, page={}", userId, pageable.getPageNumber());

    Page<PostEntity> postPage = postRepository
        .findByUserIdAndIsPublishedTrueOrderByCreatedAtDesc(userId, pageable);

    return postPage.map(this::convertToResponseDTO);

  }

  @Override
  public Page<PostResponseDTO> getRecentPosts(Pageable pageable) {

    log.debug("최신 게시글 조회: page={}, size={}",
        pageable.getPageNumber(), pageable.getPageSize());

    // 1. Repository에서 Page<Entity> 조회
    Page<PostEntity> postPage = postRepository
        .findAllByIsPublishedTrueOrderByCreatedAtDesc(pageable);

    // 2. Page<Entity> → Page<DTO> 변환
    return postPage.map(this::convertToResponseDTO);

  }

  @Async // 비동기
  @Override
  public void incrementViewCount(Long postId) {
    viewCountService.incrementViewCount(postId);
    log.debug("조회수 증가: postId={}", postId);

  }

  private PostResponseDTO convertToResponseDTO(PostEntity post) {


    // 1. 현재 DB 조회수
    Integer dbViewCount = post.getViewCount();

    // 2. Redis 조회수 (최근 증가분)
    Long redisViewCount = viewCountService.getViewCount(post.getPostId());

    // 3. 합산
    Integer totalViewCount = dbViewCount + redisViewCount.intValue();

    log.debug("조회수 합산: postId={}, DB={}, Redis={}, Total={}",
        post.getPostId(), dbViewCount, redisViewCount, totalViewCount);

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
        .viewCount(totalViewCount)
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .build();
  }
}
