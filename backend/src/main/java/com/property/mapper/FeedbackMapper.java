package com.property.mapper;

import com.property.entity.Feedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeedbackMapper {
    
    List<Feedback> findAllByOrderByCreatedAtDesc();
    
    Feedback findById(@Param("id") Long id);
    
    int insert(Feedback feedback);
    
    int update(Feedback feedback);
    
    int deleteById(@Param("id") Long id);
}
