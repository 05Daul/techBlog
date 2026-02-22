package daulspring.blogservice.service;

import daulspring.blogservice.dto.SeriesCreateRequestDTO;
import daulspring.blogservice.dto.SeriesResponseDTO;
import daulspring.blogservice.entity.SeriesEntity;
import daulspring.blogservice.repository.SeriesRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SeriesServiceImpl implements SeriesService {

  private final SeriesRepository seriesRepository;

  @Override
  public SeriesResponseDTO createSeries(Long userId, SeriesCreateRequestDTO dto) {
    SeriesEntity series = new SeriesEntity();
    series.setUserId(userId);
    series.setName(dto.getName()); // 엔티티 필드명에 맞게 수정 필요
    
    SeriesEntity savedSeries = seriesRepository.save(series);
    return convertToResponseDTO(savedSeries);
  }

  @Override
  public SeriesResponseDTO updateSeries(Long userId, Long seriesId, SeriesCreateRequestDTO dto) {
    SeriesEntity series = seriesRepository.findById(seriesId)
        .orElseThrow(() -> new IllegalArgumentException("시리즈가 존재하지 않습니다."));

    if (!series.getUserId().equals(userId)) {
      throw new IllegalStateException("시리즈 수정 권한이 없습니다.");
    }

    series.setName(dto.getName()); // 이름 수정
    return convertToResponseDTO(series);
  }

  @Override
  public void deleteSeries(Long userId, Long seriesId) {
    SeriesEntity series = seriesRepository.findById(seriesId)
        .orElseThrow(() -> new IllegalArgumentException("시리즈가 존재하지 않습니다."));

    if (!series.getUserId().equals(userId)) {
      throw new IllegalStateException("시리즈 삭제 권한이 없습니다.");
    }

    // TODO: 삭제 시 해당 시리즈에 속한 Post들의 series_id를 null로 업데이트 하는 로직 필요
    seriesRepository.deleteById(seriesId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<SeriesResponseDTO> getUserSeries(Long userId) {
    return seriesRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
        .map(this::convertToResponseDTO)
        .collect(Collectors.toList());
  }

  private SeriesResponseDTO convertToResponseDTO(SeriesEntity series) {
    return SeriesResponseDTO.builder()
        .id(series.getId())
        .userId(series.getUserId())
        .name(series.getName())
        .createdAt(series.getCreatedAt())
        .build();
  }
}