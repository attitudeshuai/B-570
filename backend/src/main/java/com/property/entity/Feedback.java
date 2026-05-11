package com.property.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Feedback {
    
    private Long id;
    
    private String content;
    
    private String contact;
    
    private String contactName;
    
    private String status = "pending"; // pending, processing, resolved
    
    private String type;
    
    private String reply;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private FeedbackRating rating;
}
