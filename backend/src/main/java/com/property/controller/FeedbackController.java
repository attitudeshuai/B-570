package com.property.controller;

import com.property.dto.ApiResponse;
import com.property.dto.FeedbackDTO;
import com.property.dto.FeedbackRatingDTO;
import com.property.dto.FeedbackReplyDTO;
import com.property.dto.RatingStatisticsDTO;
import com.property.entity.Feedback;
import com.property.entity.FeedbackRating;
import com.property.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {
    
    private final FeedbackService feedbackService;
    
    @GetMapping
    public ApiResponse<List<Feedback>> getAllFeedbacks() {
        return ApiResponse.success(feedbackService.getAllFeedbacks());
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Feedback> getFeedbackById(@PathVariable Long id) {
        return ApiResponse.success(feedbackService.getFeedbackById(id));
    }
    
    @PostMapping
    public ApiResponse<Feedback> createFeedback(@RequestBody FeedbackDTO dto) {
        return ApiResponse.success("反馈提交成功", feedbackService.createFeedback(dto));
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Feedback> replyFeedback(@PathVariable Long id, @RequestBody FeedbackReplyDTO dto) {
        return ApiResponse.success("处理成功", feedbackService.replyFeedback(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ApiResponse.success("删除成功", null);
    }
    
    @PostMapping("/rating")
    public ApiResponse<FeedbackRating> submitRating(@RequestBody FeedbackRatingDTO dto) {
        return ApiResponse.success("评分成功", feedbackService.submitRating(dto));
    }
    
    @GetMapping("/statistics/rating")
    public ApiResponse<List<RatingStatisticsDTO>> getRatingStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ApiResponse.success(feedbackService.getRatingStatistics(startTime, endTime));
    }
}
