package com.property.mapper;

import com.property.entity.Rating;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface RatingMapper {

    int insert(Rating rating);

    Rating findByFeedbackId(@Param("feedbackId") Long feedbackId);

    List<Map<String, Object>> getStatsByType(@Param("startDate") String startDate,
                                              @Param("endDate") String endDate);
}
