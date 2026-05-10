package com.property.service;

import com.property.dto.FeedbackDTO;
import com.property.dto.FeedbackReplyDTO;
import com.property.entity.Feedback;
import com.property.mapper.FeedbackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    
    private final FeedbackMapper feedbackMapper;
    
    public List<Feedback> getAllFeedbacks() {
        return feedbackMapper.findAllByOrderByCreatedAtDesc();
    }
    
    public Feedback getFeedbackById(Long id) {
        Feedback feedback = feedbackMapper.findById(id);
        if (feedback == null) {
            throw new RuntimeException("反馈不存在");
        }
        return feedback;
    }
    
    public Feedback createFeedback(FeedbackDTO dto) {
        Feedback feedback = new Feedback();
        feedback.setContent(dto.getContent());
        feedback.setContact(dto.getContact());
        feedback.setContactName(dto.getContactName());
        feedback.setType(dto.getType() != null ? dto.getType() : "其他");
        feedback.setStatus("pending");
        feedbackMapper.insert(feedback);
        return feedback;
    }
    
    public Feedback replyFeedback(Long id, FeedbackReplyDTO dto) {
        Feedback feedback = getFeedbackById(id);
        feedback.setStatus(dto.getStatus());
        feedback.setReply(dto.getReply());
        if (dto.getType() != null) {
            feedback.setType(dto.getType());
        }
        feedbackMapper.update(feedback);
        return feedback;
    }
    
    public void deleteFeedback(Long id) {
        feedbackMapper.deleteById(id);
    }
}
