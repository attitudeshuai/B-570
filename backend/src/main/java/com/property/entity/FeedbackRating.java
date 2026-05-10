package com.property.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FeedbackRating {

    private Long id;

    private Long feedbackId;

    private Integer rating;

    private LocalDateTime createdAt;
}
