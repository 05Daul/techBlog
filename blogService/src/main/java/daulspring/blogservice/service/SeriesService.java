package daulspring.blogservice.service;

import daulspring.blogservice.dto.SeriesCreateRequestDTO;
import daulspring.blogservice.dto.SeriesResponseDTO;
import java.util.List;

public interface SeriesService {

  SeriesResponseDTO createSeries(Long userId, SeriesCreateRequestDTO dto);
  SeriesResponseDTO updateSeries(Long userId, Long seriesId, SeriesCreateRequestDTO dto);
  void deleteSeries(Long userId, Long seriesId);
  List<SeriesResponseDTO> getUserSeries(Long userId);

}
