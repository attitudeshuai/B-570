package com.property.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Rating {

    private Long id;

    private Long feedbackId;

    private Integer score;

    private LocalDateTime createdAt;
}
