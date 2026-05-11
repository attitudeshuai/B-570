package com.property.mapper;

import com.property.dto.RatingStatisticsDTO;
import com.property.entity.FeedbackRating;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface FeedbackRatingMapper {
    
    FeedbackRating findByFeedbackId(@Param("feedbackId") Long feedbackId);
    
    int insert(FeedbackRating rating);
    
    List<RatingStatisticsDTO> findStatisticsByDateRangeGroupByType(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
