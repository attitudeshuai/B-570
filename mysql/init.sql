-- 设置字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS property_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE property_db;

-- 设置数据库字符集
SET NAMES utf8mb4;
ALTER DATABASE property_db CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- 管理员表
CREATE TABLE IF NOT EXISTS admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 反馈表
CREATE TABLE IF NOT EXISTS feedback (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(1000) NOT NULL,
    contact VARCHAR(100),
    contact_name VARCHAR(50),
    status VARCHAR(20) DEFAULT 'pending',
    reply VARCHAR(1000),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 通知表
CREATE TABLE IF NOT EXISTS notice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content VARCHAR(2000) NOT NULL,
    type VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 管理员账号由应用程序自动初始化 (用户名: admin, 密码: admin123)

-- 插入示例通知
INSERT INTO notice (title, content, type) VALUES 
('停水通知', '尊敬的业主：因市政管网维护，本小区将于明日（1月22日）8:00-18:00停水，请各位业主提前做好储水准备，给您带来的不便敬请谅解。', '停水'),
('电梯维护公告', '尊敬的业主：1号楼电梯将于本周六进行例行维护保养，预计时间为上午9:00-12:00，届时电梯将暂停运行，请您合理安排出行时间。', '维修'),
('物业费缴纳通知', '尊敬的业主：2024年第一季度物业费已开始收取，请各位业主于本月25日前完成缴纳。可通过物业中心或线上渠道进行缴费，感谢您的配合！', '其他');

-- 插入示例反馈
INSERT INTO feedback (content, contact, contact_name, status, reply) VALUES 
('小区门口的路灯坏了好几天了，晚上很黑，希望尽快维修。', '13800138001', '张先生', 'resolved', '您好，路灯已于昨日修复完成，感谢您的反馈！'),
('希望能增加小区内的垃圾桶数量，特别是在健身区附近。', '13800138002', '李女士', 'processing', NULL),
('楼道的消防设施好像过期了，请检查一下。', NULL, NULL, 'pending', NULL);
