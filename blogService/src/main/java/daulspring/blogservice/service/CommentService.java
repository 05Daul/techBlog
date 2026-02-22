package daulspring.blogservice.service;

import daulspring.blogservice.dto.CommentCreateRequestDTO;
import daulspring.blogservice.dto.CommentResponseDTO;
import daulspring.blogservice.dto.CommentUpdateRequestDTO;
import java.util.List;

public interface CommentService {

  CommentResponseDTO createComment(Long userId, CommentCreateRequestDTO dto);
  CommentResponseDTO updateComment(Long userId, CommentUpdateRequestDTO dto);
  void deleteComment(Long userId, Long commentId);
  CommentResponseDTO getCommentById(Long commentId);
  List<CommentResponseDTO> getCommentsByPostId(Long postId);
  Long getCommentCount(Long postId);
}