package com.property.service;

import com.property.entity.Admin;
import com.property.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    
    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    
    public Admin login(String username, String password) {
        Admin admin = adminMapper.findByUsername(username);
        if (admin == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        return admin;
    }
    
    public void initAdmin() {
        Admin admin = adminMapper.findByUsername("admin");
        if (admin == null) {
            // 创建新管理员
            admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            adminMapper.insert(admin);
        } else {
            // 确保密码正确（修复可能的哈希值问题）
            if (!passwordEncoder.matches("admin123", admin.getPassword())) {
                admin.setPassword(passwordEncoder.encode("admin123"));
                adminMapper.update(admin);
            }
        }
    }
}
