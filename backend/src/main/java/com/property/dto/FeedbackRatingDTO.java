package com.property.dto;

import lombok.Data;

@Data
public class FeedbackRatingDTO {
    
    private Long feedbackId;
    
    private Integer score;
    
    private String comment;
}
