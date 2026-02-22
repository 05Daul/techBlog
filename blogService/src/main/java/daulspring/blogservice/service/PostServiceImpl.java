package daulspring.blogservice.service;

import daulspring.blogservice.dto.PostCreateRequestDTO;
import daulspring.blogservice.dto.PostResponseDTO;
import daulspring.blogservice.dto.PostUpdateRequestDTO;
import daulspring.blogservice.entity.PostEntity;
import daulspring.blogservice.entity.PostTagEntity;
import daulspring.blogservice.entity.PostTagId;
import daulspring.blogservice.entity.TagEntity;
import daulspring.blogservice.repository.CommentRepository;
import daulspring.blogservice.repository.LikeRepository;
import daulspring.blogservice.repository.PostRepository;
import daulspring.blogservice.repository.PostTagRepository;
import daulspring.blogservice.repository.TagRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final TagRepository tagRepository;
  private final PostTagRepository postTagRepository;
  private final CommentRepository commentRepository;
  private final LikeRepository likeRepository;

  @Override
  public PostResponseDTO createPost(Long userId, PostCreateRequestDTO dto) {
    PostEntity post = new PostEntity();
    post.setUserId(userId);
    post.setTitle(dto.getTitle());
    post.setContent(dto.getContent());
    post.setThumbnail(dto.getThumbnail());
    post.setIsPublished(dto.getIsPublished());
    post.setSeriesId(dto.getSeriesId());
    post.setIsCrawled(false);

    PostEntity savedPost = postRepository.save(post);

    // 태그 추가
    if (dto.getTags() != null && !dto.getTags().isEmpty()) {
      addTagsToPost(savedPost.getPostId(), dto.getTags());
    }

    return convertToResponseDTO(savedPost);
  }

  @Override
  public PostResponseDTO updatePost(Long userId, Long postId, PostUpdateRequestDTO dto) {
    PostEntity post = postRepository.findById(postId)
        .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

    if (!post.getUserId().equals(userId)) {
      throw new IllegalStateException("게시글 수정 권한이 없습니다.");
    }

    post.setTitle(dto.getTitle());
    post.setContent(dto.getContent());
    post.setThumbnail(dto.getThumbnail());
    if (dto.getIsPublished() != null) {
      post.setIsPublished(dto.getIsPublished());
    }
    post.setSeriesId(dto.getSeriesId());

    // 태그 재설정
    if (dto.getTags() != null) {
      postTagRepository.deleteByPostTagId_PostId(postId);
      if (!dto.getTags().isEmpty()) {
        addTagsToPost(postId, dto.getTags());
      }
    }

    return convertToResponseDTO(post);
  }

  @Override
  public void deletePost(Long userId, Long postId) {
    PostEntity post = postRepository.findById(postId)
        .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

    if (!post.getUserId().equals(userId)) {
      throw new IllegalStateException("게시글 삭제 권한이 없습니다.");
    }

    postTagRepository.deleteByPostTagId_PostId(postId);
    postRepository.deleteById(postId);
  }

  @Override
  @Transactional(readOnly = true)
  public PostResponseDTO getPostById(Long postId) {
    PostEntity post = postRepository.findById(postId)
        .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
    return convertToResponseDTO(post);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PostResponseDTO> getRecentPosts(Pageable pageable) {
    return postRepository.findAllByIsPublishedTrueOrderByCreatedAtDesc(pageable)
        .map(this::convertToResponseDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PostResponseDTO> getTrendingPosts(Pageable pageable) {
    LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
    return postRepository.findTrendingPosts(sevenDaysAgo, pageable)
        .map(this::convertToResponseDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PostResponseDTO> getFeedPosts(Long userId, Pageable pageable) {
    // TODO: User 서비스에서 팔로우 목록 가져오기 (Feign or gRPC)
    List<Long> followingUserIds = List.of(); // 임시
    return postRepository.findFeedPostsByUserIds(followingUserIds, pageable)
        .map(this::convertToResponseDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PostResponseDTO> getMyPosts(Long userId, Pageable pageable) {
    return postRepository.findByUserIdAndIsPublishedTrue(userId, pageable)
        .map(this::convertToResponseDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PostResponseDTO> searchPosts(String keyword, Pageable pageable) {
    if (keyword == null || keyword.trim().isEmpty()) {
      throw new IllegalArgumentException("검색어를 입력해주세요.");
    }
    return postRepository.searchPosts(keyword.trim(), pageable)
        .map(this::convertToResponseDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public List<String> getTagsByPostId(Long postId) {
    return postTagRepository.findByPostTagId_PostId(postId).stream()
        .map(pt -> pt.getTag().getTagName())
        .collect(Collectors.toList());
  }

  @Override
  public void addTagToPost(Long postId, String tagName) {
    PostEntity post = postRepository.findById(postId)
        .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

    TagEntity tag = tagRepository.findByTagName(tagName)
        .orElseGet(() -> {
          TagEntity newTag = new TagEntity();
          newTag.setTagName(tagName);
          return tagRepository.save(newTag);
        });

    if (postTagRepository.existsByPostTagId_PostIdAndPostTagId_TagId(postId, tag.getTagId())) {
      throw new IllegalStateException("이미 추가된 태그입니다.");
    }

    PostTagId postTagId = new PostTagId(postId, tag.getTagId());
    PostTagEntity postTag = new PostTagEntity();
    postTag.setPostTagId(postTagId); // 복합키 세팅
    postTag.setPost(post);
    postTag.setTag(tag);

    postTagRepository.save(postTag);
  }

  @Override
  public void addTagsToPost(Long postId, List<String> tagNames) {
    if (tagNames == null || tagNames.isEmpty()) {
      return;
    }

    for (String tagName : tagNames) {
      try {
        addTagToPost(postId, tagName);
      } catch (IllegalStateException e) {
        // 중복 태그는 무시
      }
    }
  }

  @Override
  public void removeTagFromPost(Long postId, String tagName) {
    TagEntity tag = tagRepository.findByTagName(tagName)
        .orElseThrow(() -> new IllegalArgumentException("태그가 존재하지 않습니다."));
    postTagRepository.deleteByPostTagId_PostIdAndPostTagId_TagId(postId, tag.getTagId());
  }

  @Override
  public void incrementViewCount(Long postId) {
    postRepository.incrementViewCount(postId);
  }

  @Override
  public String uploadImage(MultipartFile file) {
    // TODO: GCS 또는 S3에 업로드 구현
    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    log.info("파일 업로드: {}", fileName);
    return "https://example.com/images/" + fileName;
  }

  private PostResponseDTO convertToResponseDTO(PostEntity post) {
    List<String> tags = getTagsByPostId(post.getPostId());
    Long commentCount = commentRepository.countByPostIdAndIsDeletedFalse(post.getPostId());
    Long likeCount = likeRepository.countByPostId(post.getPostId());

    return PostResponseDTO.builder()
        .id(post.getPostId())
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
        .tags(tags)
        .commentCount(commentCount)
        .likeCount(likeCount)
        .build();
  }
}