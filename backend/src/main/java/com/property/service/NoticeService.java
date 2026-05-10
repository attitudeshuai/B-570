package com.property.service;

import com.property.dto.NoticeDTO;
import com.property.entity.Notice;
import com.property.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    
    private final NoticeMapper noticeMapper;
    
    public List<Notice> getAllNotices() {
        return noticeMapper.findAllByOrderByCreatedAtDesc();
    }
    
    public Notice getNoticeById(Long id) {
        Notice notice = noticeMapper.findById(id);
        if (notice == null) {
            throw new RuntimeException("通知不存在");
        }
        return notice;
    }
    
    public Notice createNotice(NoticeDTO dto) {
        Notice notice = new Notice();
        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setType(dto.getType());
        noticeMapper.insert(notice);
        return notice;
    }
    
    public Notice updateNotice(Long id, NoticeDTO dto) {
        Notice notice = getNoticeById(id);
        notice.setTitle(dto.getTitle());
        notice.setContent(dto.getContent());
        notice.setType(dto.getType());
        noticeMapper.update(notice);
        return notice;
    }
    
    public void deleteNotice(Long id) {
        noticeMapper.deleteById(id);
    }
}
