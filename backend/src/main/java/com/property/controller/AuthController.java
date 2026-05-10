package com.property.controller;

import com.property.dto.ApiResponse;
import com.property.dto.LoginDTO;
import com.property.entity.Admin;
import com.property.service.AdminService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AdminService adminService;
    
    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody LoginDTO dto, HttpSession session) {
        try {
            Admin admin = adminService.login(dto.getUsername(), dto.getPassword());
            session.setAttribute("admin", admin);
            
            Map<String, Object> data = new HashMap<>();
            data.put("id", admin.getId());
            data.put("username", admin.getUsername());
            data.put("token", session.getId());
            
            return ApiResponse.success("登录成功", data);
        } catch (Exception e) {
            return ApiResponse.error(401, e.getMessage());
        }
    }
    
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpSession session) {
        session.invalidate();
        return ApiResponse.success("退出成功", null);
    }
    
    @GetMapping("/check")
    public ApiResponse<Map<String, Object>> checkLogin(HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", admin.getId());
            data.put("username", admin.getUsername());
            return ApiResponse.success(data);
        }
        return ApiResponse.error(401, "未登录");
    }
}
