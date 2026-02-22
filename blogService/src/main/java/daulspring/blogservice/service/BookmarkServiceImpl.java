package daulspring.blogservice.service;

import daulspring.blogservice.dto.BookmarkResponseDTO;
import daulspring.blogservice.entity.BookmarkEntity;
import daulspring.blogservice.entity.PostEntity;
import daulspring.blogservice.repository.BookmarkRepository;
import daulspring.blogservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
  private final BookmarkRepository bookmarkRepository;
  private final PostRepository postRepository;

  @Override
  public void addBookmark(Long userId, Long postId) {
    if (bookmarkRepository.existsByUserIdAndPostId(userId, postId)) {
      throw new IllegalStateException("이미 북마크한 게시글입니다.");
    }

    // 게시글 존재 여부 검증
    PostEntity post = postRepository.findById(postId)
        .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

    BookmarkEntity bookmark = new BookmarkEntity();
    bookmark.setUserId(userId);
    bookmark.setPostId(postId);
    // 필요하다면 연관관계 매핑 (bookmark.setPost(post))

    bookmarkRepository.save(bookmark);
  }

  @Override
  public void removeBookmark(Long userId, Long postId) {
    BookmarkEntity bookmark = bookmarkRepository.findByUserIdAndPostId(userId, postId)
        .orElseThrow(() -> new IllegalArgumentException("북마크 내역이 존재하지 않습니다."));

    bookmarkRepository.delete(bookmark);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<BookmarkResponseDTO> getUserBookmarks(Long userId, Pageable pageable) {
    return bookmarkRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
        .map(bookmark -> BookmarkResponseDTO.builder()
            .id(bookmark.getBookmarkId())
            .userId(bookmark.getUserId())
            .postId(bookmark.getPostId())
            // Post 데이터를 조인하여 제목, 썸네일 등을 DTO에 담아주는 로직이 추가되면 좋습니다.
            .createdAt(bookmark.getCreatedAt())
            .build());
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isBookmarked(Long userId, Long postId) {
    return bookmarkRepository.existsByUserIdAndPostId(userId, postId);
  }


}
