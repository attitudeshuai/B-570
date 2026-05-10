package com.property.controller;

import com.property.dto.ApiResponse;
import com.property.dto.NoticeDTO;
import com.property.entity.Notice;
import com.property.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {
    
    private final NoticeService noticeService;
    
    @GetMapping
    public ApiResponse<List<Notice>> getAllNotices() {
        return ApiResponse.success(noticeService.getAllNotices());
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Notice> getNoticeById(@PathVariable Long id) {
        return ApiResponse.success(noticeService.getNoticeById(id));
    }
    
    @PostMapping
    public ApiResponse<Notice> createNotice(@RequestBody NoticeDTO dto) {
        return ApiResponse.success("通知发布成功", noticeService.createNotice(dto));
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Notice> updateNotice(@PathVariable Long id, @RequestBody NoticeDTO dto) {
        return ApiResponse.success("更新成功", noticeService.updateNotice(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ApiResponse.success("删除成功", null);
    }
}
