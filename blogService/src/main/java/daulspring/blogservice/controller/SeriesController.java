package daulspring.blogservice.controller;

import daulspring.blogservice.dto.SeriesCreateRequestDTO;
import daulspring.blogservice.dto.SeriesResponseDTO;
import daulspring.blogservice.service.SeriesService;
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
@RequestMapping("/blog/series")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    @PostMapping
    public ResponseEntity<SeriesResponseDTO> createSeries(@RequestParam Long userId, @RequestBody SeriesCreateRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seriesService.createSeries(userId, dto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SeriesResponseDTO>> getUserSeries(@PathVariable Long userId) {
        return ResponseEntity.ok(seriesService.getUserSeries(userId));
    }

    @DeleteMapping("/{seriesId}")
    public ResponseEntity<Void> deleteSeries(@RequestParam Long userId, @PathVariable Long seriesId) {
        seriesService.deleteSeries(userId, seriesId);
        return ResponseEntity.noContent().build();
    }
}