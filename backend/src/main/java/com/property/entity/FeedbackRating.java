package com.property.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FeedbackRating {
    
    private Long id;
    
    private Long feedbackId;
    
    private Integer score;
    
    private String comment;
    
    private LocalDateTime createdAt;
}
