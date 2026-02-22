package daulspring.blogservice.service;

import daulspring.blogservice.dto.CommentCreateRequestDTO;
import daulspring.blogservice.dto.CommentResponseDTO;
import daulspring.blogservice.dto.CommentUpdateRequestDTO;
import daulspring.blogservice.entity.CommentEntity;
import daulspring.blogservice.repository.CommentRepository;
import daulspring.blogservice.repository.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;

  @Override
  public CommentResponseDTO createComment(Long userId, CommentCreateRequestDTO dto) {
    // 게시글 존재 확인
    postRepository.findById(dto.getPostId())
        .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

    CommentEntity comment = new CommentEntity();
    comment.setPostId(dto.getPostId());
    comment.setUserId(userId);
    comment.setContent(dto.getContent());
    comment.setIsDeleted(false);

    // 대댓글인 경우
    if (dto.getParentId() != null) {
      CommentEntity parent = commentRepository.findById(dto.getParentId())
          .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));
      comment.setParentComment(parent);
    }

    CommentEntity savedComment = commentRepository.save(comment);
    return convertToResponseDTO(savedComment);
  }


  @Override
  public CommentResponseDTO updateComment(Long userId, CommentUpdateRequestDTO dto) {
    CommentEntity comment = commentRepository.findById(dto.getCommentId())
        .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

    if (!comment.getUserId().equals(userId)) {
      throw new IllegalStateException("댓글 수정 권한이 없습니다.");
    }

    if (comment.getIsDeleted()) {
      throw new IllegalStateException("삭제된 댓글은 수정할 수 없습니다.");
    }

    comment.setContent(dto.getContent());
    return convertToResponseDTO(comment);
  }

  @Override
  public void deleteComment(Long userId, Long commentId) {
    CommentEntity comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

    if (!comment.getUserId().equals(userId)) {
      throw new IllegalStateException("댓글 삭제 권한이 없습니다.");
    }

    // 대댓글이 있으면 소프트 삭제, 없으면 실제 삭제
    if (!comment.getChildComments().isEmpty()) {
      comment.setContent("삭제된 댓글입니다.");
      comment.setIsDeleted(true);
    } else {
      commentRepository.deleteById(commentId);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public CommentResponseDTO getCommentById(Long commentId) {
    CommentEntity comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
    return convertToResponseDTOWithChildren(comment);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CommentResponseDTO> getCommentsByPostId(Long postId) {
    List<CommentEntity> topLevelComments = commentRepository.findTopLevelCommentsByPostId(postId);
    return topLevelComments.stream()
        .map(this::convertToResponseDTOWithChildren)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Long getCommentCount(Long postId) {
    return commentRepository.countByPostIdAndIsDeletedFalse(postId);
  }

  private CommentResponseDTO convertToResponseDTO(CommentEntity comment) {
    return CommentResponseDTO.builder()
        .id(comment.getCommentId())
        .postId(comment.getPostId())
        .userId(comment.getUserId())
        .parentId(comment.getParentComment() != null ? comment.getParentComment().getCommentId() : null)
        .content(comment.getContent())
        .isDeleted(comment.getIsDeleted())
        .createdAt(comment.getCreatedAt())
        .updatedAt(comment.getUpdatedAt())
        .childCount(0)
        .build();
  }

  private CommentResponseDTO convertToResponseDTOWithChildren(CommentEntity comment) {
    List<CommentResponseDTO> children = comment.getChildComments().stream()
        .map(this::convertToResponseDTOWithChildren)
        .collect(Collectors.toList());

    return CommentResponseDTO.builder()
        .id(comment.getCommentId())
        .postId(comment.getPostId())
        .userId(comment.getUserId())
        .parentId(comment.getParentComment() != null ? comment.getParentComment().getCommentId() : null)
        .content(comment.getContent())
        .isDeleted(comment.getIsDeleted())
        .createdAt(comment.getCreatedAt())
        .updatedAt(comment.getUpdatedAt())
        .childComments(children)
        .childCount(children.size())
        .build();
  }
}