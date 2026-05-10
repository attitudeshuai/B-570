package com.property.mapper;

import com.property.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {
    
    List<Notice> findAllByOrderByCreatedAtDesc();
    
    Notice findById(@Param("id") Long id);
    
    int insert(Notice notice);
    
    int update(Notice notice);
    
    int deleteById(@Param("id") Long id);
}
