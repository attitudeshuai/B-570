package com.property.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Feedback {
    
    private Long id;
    
    private String content;
    
    private String contact;
    
    private String contactName;
    
    private String type = "其他";
    
    private String status = "pending"; // pending, processing, resolved
    
    private String reply;
    
    private Integer ratingScore;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
