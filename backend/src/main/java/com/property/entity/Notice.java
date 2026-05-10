package com.property.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Notice {
    
    private Long id;
    
    private String title;
    
    private String content;
    
    private String type; // 停水, 停电, 维修, 其他
    
    private LocalDateTime createdAt;
}
