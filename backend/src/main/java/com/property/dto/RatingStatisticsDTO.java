package com.property.dto;

import lombok.Data;

@Data
public class RatingStatisticsDTO {

    private String feedbackType;

    private Long totalCount;

    private Double averageRating;

    private Long negativeCount;

    private Double negativeRate;
}
