package com.property.controller;

import com.property.dto.ApiResponse;
import com.property.dto.RatingDTO;
import com.property.entity.Rating;
import com.property.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ApiResponse<Rating> submitRating(@RequestBody RatingDTO dto) {
        return ApiResponse.success("评分提交成功", ratingService.submitRating(dto));
    }

    @GetMapping("/feedback/{feedbackId}")
    public ApiResponse<Rating> getRatingByFeedbackId(@PathVariable Long feedbackId) {
        return ApiResponse.success(ratingService.getRatingByFeedbackId(feedbackId));
    }

    @GetMapping("/stats")
    public ApiResponse<List<Map<String, Object>>> getStats(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ApiResponse.success(ratingService.getStatsByType(startDate, endDate));
    }
}
