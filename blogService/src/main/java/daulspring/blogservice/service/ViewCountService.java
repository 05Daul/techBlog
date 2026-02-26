package daulspring.blogservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ViewCountService {

  private final RedisTemplate<String, String> redisTemplate;

  private static final String VIEW_COUNT_KEY_PREFIX = "post:";
  private static final String VIEW_COUNT_KEY_SUFFIX = ":views";

  /**
   * Redis에서 조회수 증가
   *
   */
  public void incrementViewCount(Long postId) {
    String key = getViewCountKey(postId); // post:postid:view
    redisTemplate.opsForValue().increment(key);  // post:postid:view = 1 이렇게 증가
    log.debug("Redis 조회수 증가: postId={}, key={}", postId, key);
  }

  /**
   * Redis에서 조회수 조회
   */
  public Long getViewCount(Long postId) {
    String key = getViewCountKey(postId);
    String value = redisTemplate.opsForValue().get(key);

    if (value == null) {
      return 0L;
    }

    try {
      return Long.parseLong(value);
    } catch (NumberFormatException e) {
      log.warn("Redis 조회수 파싱 실패: postId={}, value={}", postId, value);
      return 0L;
    }
  }

  /**
   * Redis 조회수를 원자적으로 가져오고 삭제
   * GETDEL 명령 사용 (원자적 연산)
   */
  public Long getAndResetViewCount(Long postId) {
    String key = getViewCountKey(postId);

    String value = redisTemplate.execute(
        //execute()는 RedisCallback의 실행기
        (RedisCallback<String>) connection -> {
      byte[] keyBytes = key.getBytes();

      byte[] valueBytes = connection.getDel(keyBytes);  // ← GETDEL

      return valueBytes != null ? new String(valueBytes) : null; //value로 반환
    });

    if (value == null) {
      return 0L;
    }

    try {
      Long viewCount = Long.parseLong(value);
      log.debug("Redis 조회수 원자적 추출: postId={}, count={}", postId, viewCount);
      return viewCount;
    } catch (NumberFormatException e) {
      log.warn("Redis 조회수 파싱 실패: postId={}, value={}", postId, value);
      return 0L;
    }
  }

  private String getViewCountKey(Long postId) {
    ///  post:postid:view
    return VIEW_COUNT_KEY_PREFIX + postId + VIEW_COUNT_KEY_SUFFIX;
  }
}
