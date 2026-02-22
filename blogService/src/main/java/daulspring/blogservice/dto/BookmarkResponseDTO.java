package daulspring.blogservice.dto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkResponseDTO {
    private Long id;
    private Long userId;
    private Long postId;
    
    // 선택 사항: 목록 조회 시 편의를 위한 게시글 정보
    private String postTitle;
    private String postThumbnail;
    
    private LocalDateTime createdAt;
}