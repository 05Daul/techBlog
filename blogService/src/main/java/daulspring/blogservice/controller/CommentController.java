package daulspring.blogservice.controller;

import daulspring.blogservice.dto.CommentCreateRequestDTO;
import daulspring.blogservice.dto.CommentResponseDTO;
import daulspring.blogservice.dto.CommentUpdateRequestDTO;
import daulspring.blogservice.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blog/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(@RequestParam Long userId, @RequestBody CommentCreateRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(userId, dto));
    }


    @PostMapping
    public ResponseEntity<CommentResponseDTO> updateComment(@RequestParam Long userId, @RequestBody CommentUpdateRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.updateComment(userId, dto));
    }



    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@RequestParam Long userId, @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
        return ResponseEntity.noContent().build();
    }
}