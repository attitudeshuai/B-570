package com.property.mapper;

import com.property.dto.RatingStatisticsDTO;
import com.property.entity.FeedbackRating;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FeedbackRatingMapper {

    void insert(FeedbackRating rating);

    FeedbackRating findByFeedbackId(Long feedbackId);

    List<RatingStatisticsDTO> findStatistics(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
