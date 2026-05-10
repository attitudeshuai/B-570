package com.property.controller;

import com.property.dto.ApiResponse;
import com.property.dto.FeedbackRatingDTO;
import com.property.dto.RatingStatisticsDTO;
import com.property.entity.FeedbackRating;
import com.property.service.FeedbackRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class FeedbackRatingController {

    private final FeedbackRatingService feedbackRatingService;

    @PostMapping
    public ApiResponse<FeedbackRating> createRating(@RequestBody FeedbackRatingDTO dto) {
        return ApiResponse.success("评分成功", feedbackRatingService.createRating(dto));
    }

    @GetMapping("/statistics")
    public ApiResponse<List<RatingStatisticsDTO>> getStatistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ApiResponse.success(feedbackRatingService.getStatistics(startTime, endTime));
    }
}
