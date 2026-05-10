package com.property.config;

import com.property.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final AdminService adminService;
    
    @Override
    public void run(String... args) {
        adminService.initAdmin();
        System.out.println("=================================");
        System.out.println("默认管理员账号已初始化");
        System.out.println("用户名: admin");
        System.out.println("密码: admin123");
        System.out.println("=================================");
    }
}
