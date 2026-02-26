package daulspring.blogservice.scheduler;

import daulspring.blogservice.repository.PostRepository;
import daulspring.blogservice.service.ViewCountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ViewCountSyncScheduler {

  private final RedisTemplate<String, String> redisTemplate;
  private final PostRepository postRepository;
  private final ViewCountService viewCountService;

  /**
   * 1분마다 Redis → DB 동기화
   */
  @Scheduled(cron = "0 * * * * *")
  @Transactional
  public void syncViewCounts() {

    log.info("조회수 동기화 시작");

    int syncCount = 0;

    // SCAN으로 post:*:views 패턴의 키를 순회
    ScanOptions scanOptions = ScanOptions.scanOptions()
        .match("post:*:views")
        .count(100)  // 한 번에 100개씩 스캔
        .build();

    try (Cursor<byte[]> cursor = redisTemplate.getConnectionFactory()
        .getConnection()
        .scan(scanOptions)) {

      while (cursor.hasNext()) {
        String key = new String(cursor.next());

        try {
          // post:1:views → postId 추출
          Long postId = extractPostId(key);

          // Redis 조회수 가져오고 원자적으로 삭제
          Long viewCount = viewCountService.getAndResetViewCount(postId);

          if (viewCount > 0) {
            // DB에 조회수 증가
            int updated = postRepository.incrementViewCount(postId, viewCount);

            if (updated > 0) {
              syncCount++;
              log.debug("조회수 동기화 성공: postId={}, count={}", postId, viewCount);
            } else {
              log.warn("게시글 없음: postId={}", postId);
            }
          }

        } catch (Exception e) {
          log.error("조회수 동기화 실패: key={}", key, e);
        }
      }

    } catch (Exception e) {
      log.error("SCAN 실행 실패", e);
    }

    log.info("조회수 동기화 완료: {}건", syncCount);
  }

  private Long extractPostId(String key) {
    String[] parts = key.split(":");
    return Long.parseLong(parts[1]);
  }
}