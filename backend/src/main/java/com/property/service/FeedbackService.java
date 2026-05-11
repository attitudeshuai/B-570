package com.property.service;

import com.property.dto.FeedbackDTO;
import com.property.dto.FeedbackRatingDTO;
import com.property.dto.FeedbackReplyDTO;
import com.property.dto.RatingStatisticsDTO;
import com.property.entity.Feedback;
import com.property.entity.FeedbackRating;
import com.property.mapper.FeedbackMapper;
import com.property.mapper.FeedbackRatingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRatingMapper feedbackRatingMapper;
    
    public List<Feedback> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackMapper.findAllByOrderByCreatedAtDesc();
        for (Feedback feedback : feedbacks) {
            feedback.setRating(feedbackRatingMapper.findByFeedbackId(feedback.getId()));
        }
        return feedbacks;
    }
    
    public Feedback getFeedbackById(Long id) {
        Feedback feedback = feedbackMapper.findById(id);
        if (feedback == null) {
            throw new RuntimeException("反馈不存在");
        }
        feedback.setRating(feedbackRatingMapper.findByFeedbackId(id));
        return feedback;
    }
    
    public Feedback createFeedback(FeedbackDTO dto) {
        Feedback feedback = new Feedback();
        feedback.setContent(dto.getContent());
        feedback.setContact(dto.getContact());
        feedback.setContactName(dto.getContactName());
        feedback.setStatus("pending");
        feedbackMapper.insert(feedback);
        return feedback;
    }
    
    public Feedback replyFeedback(Long id, FeedbackReplyDTO dto) {
        Feedback feedback = getFeedbackById(id);
        feedback.setStatus(dto.getStatus());
        feedback.setReply(dto.getReply());
        feedbackMapper.update(feedback);
        return feedback;
    }
    
    public void deleteFeedback(Long id) {
        feedbackMapper.deleteById(id);
    }
    
    @Transactional
    public FeedbackRating submitRating(FeedbackRatingDTO dto) {
        Feedback feedback = getFeedbackById(dto.getFeedbackId());
        if (!"resolved".equals(feedback.getStatus())) {
            throw new RuntimeException("仅已解决的反馈可以评分");
        }
        FeedbackRating existing = feedbackRatingMapper.findByFeedbackId(dto.getFeedbackId());
        if (existing != null) {
            throw new RuntimeException("该反馈已评分，不可重复评分");
        }
        if (dto.getScore() < 1 || dto.getScore() > 5) {
            throw new RuntimeException("评分必须在1-5分之间");
        }
        FeedbackRating rating = new FeedbackRating();
        rating.setFeedbackId(dto.getFeedbackId());
        rating.setScore(dto.getScore());
        rating.setComment(dto.getComment());
        feedbackRatingMapper.insert(rating);
        return rating;
    }
    
    public List<RatingStatisticsDTO> getRatingStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        return feedbackRatingMapper.findStatisticsByDateRangeGroupByType(startTime, endTime);
    }
}
