package com.property.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Feedback {

    private Long id;

    private String content;

    private String contact;

    private String contactName;

    private String type;

    private String status = "pending";

    private String reply;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer rating;
}
