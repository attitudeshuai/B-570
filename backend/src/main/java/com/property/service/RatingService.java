package com.property.service;

import com.property.dto.RatingDTO;
import com.property.entity.Feedback;
import com.property.entity.Rating;
import com.property.mapper.FeedbackMapper;
import com.property.mapper.RatingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingMapper ratingMapper;
    private final FeedbackMapper feedbackMapper;

    @Transactional
    public Rating submitRating(RatingDTO dto) {
        Feedback feedback = feedbackMapper.findById(dto.getFeedbackId());
        if (feedback == null) {
            throw new RuntimeException("反馈不存在");
        }
        if (!"resolved".equals(feedback.getStatus())) {
            throw new RuntimeException("只能在已解决状态下评分");
        }
        Rating existing = ratingMapper.findByFeedbackId(dto.getFeedbackId());
        if (existing != null) {
            throw new RuntimeException("该反馈已评分，不能重复评分");
        }
        if (dto.getScore() < 1 || dto.getScore() > 5) {
            throw new RuntimeException("评分必须在1-5之间");
        }
        Rating rating = new Rating();
        rating.setFeedbackId(dto.getFeedbackId());
        rating.setScore(dto.getScore());
        ratingMapper.insert(rating);
        return rating;
    }

    public Rating getRatingByFeedbackId(Long feedbackId) {
        return ratingMapper.findByFeedbackId(feedbackId);
    }

    public List<Map<String, Object>> getStatsByType(String startDate, String endDate) {
        return ratingMapper.getStatsByType(startDate, endDate);
    }
}
