package daulspring.blogservice.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeriesResponseDTO {

  private Long id;
  private Long userId;
  private String name;
  private LocalDateTime createdAt;
  private Integer postCount;
}