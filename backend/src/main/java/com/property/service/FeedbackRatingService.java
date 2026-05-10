package com.property.service;

import com.property.dto.FeedbackRatingDTO;
import com.property.dto.RatingStatisticsDTO;
import com.property.entity.Feedback;
import com.property.entity.FeedbackRating;
import com.property.mapper.FeedbackMapper;
import com.property.mapper.FeedbackRatingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackRatingService {

    private final FeedbackRatingMapper feedbackRatingMapper;
    private final FeedbackMapper feedbackMapper;

    public FeedbackRating createRating(FeedbackRatingDTO dto) {
        Feedback feedback = feedbackMapper.findById(dto.getFeedbackId());
        if (feedback == null) {
            throw new RuntimeException("反馈不存在");
        }
        if (!"resolved".equals(feedback.getStatus())) {
            throw new RuntimeException("只能对已解决的反馈进行评分");
        }
        FeedbackRating existing = feedbackRatingMapper.findByFeedbackId(dto.getFeedbackId());
        if (existing != null) {
            throw new RuntimeException("该反馈已评分，不能重复评分");
        }
        if (dto.getRating() == null || dto.getRating() < 1 || dto.getRating() > 5) {
            throw new RuntimeException("评分必须在1-5分之间");
        }
        FeedbackRating rating = new FeedbackRating();
        rating.setFeedbackId(dto.getFeedbackId());
        rating.setRating(dto.getRating());
        feedbackRatingMapper.insert(rating);
        return rating;
    }

    public List<RatingStatisticsDTO> getStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        return feedbackRatingMapper.findStatistics(startTime, endTime);
    }
}
