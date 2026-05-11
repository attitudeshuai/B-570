package com.property.dto;

import lombok.Data;
import java.util.List;

@Data
public class RatingStatisticsDTO {
    
    private String type;
    
    private Long ratedCount;
    
    private Double averageScore;
    
    private Double badRate;
}

