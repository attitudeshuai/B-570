<template>
  <div class="home">
    <!-- 顶部导航 -->
    <nav class="navbar">
      <div class="nav-container">
        <div class="nav-brand">
          <i class="el-icon-office-building"></i>
          <span>物业服务中心</span>
        </div>
        <div class="nav-links">
          <router-link to="/admin/login" class="admin-link">
            <i class="el-icon-user"></i>
            <span>管理入口</span>
          </router-link>
        </div>
      </div>
    </nav>

    <!-- Hero区域 -->
    <header class="hero">
      <div class="hero-bg"></div>
      <div class="hero-content">
        <h1 class="hero-title">
          <span class="title-line">智慧物业</span>
          <span class="title-line highlight">服务平台</span>
        </h1>
        <p class="hero-subtitle">便捷反馈 · 实时通知 · 贴心服务</p>
        <div class="hero-buttons">
          <button class="btn btn-primary" @click="$router.push('/feedback')">
            <i class="el-icon-edit"></i>
            立即反馈
          </button>
          <button class="btn btn-outline" @click="$router.push('/notices')">
            <i class="el-icon-bell"></i>
            查看通知
          </button>
        </div>
      </div>
      <div class="hero-wave">
        <svg viewBox="0 0 1440 120" preserveAspectRatio="none">
          <path d="M0,64L48,69.3C96,75,192,85,288,80C384,75,480,53,576,48C672,43,768,53,864,64C960,75,1056,85,1152,80C1248,75,1344,53,1392,42.7L1440,32L1440,120L1392,120C1344,120,1248,120,1152,120C1056,120,960,120,864,120C768,120,672,120,576,120C480,120,384,120,288,120C192,120,96,120,48,120L0,120Z" fill="#f8fafc"></path>
        </svg>
      </div>
    </header>

    <!-- 功能卡片 -->
    <main class="main-content">
      <section class="features">
        <div class="section-header">
          <h2>服务功能</h2>
          <p>为您提供全方位的物业服务体验</p>
        </div>
        <div class="feature-grid">
          <div class="feature-card" @click="$router.push('/feedback')">
            <div class="card-icon feedback-icon">
              <i class="el-icon-edit-outline"></i>
            </div>
            <h3>意见反馈</h3>
            <p>提交您的意见、建议或问题，我们将及时处理并回复</p>
            <div class="card-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
          
          <div class="feature-card" @click="$router.push('/notices')">
            <div class="card-icon notice-icon">
              <i class="el-icon-bell"></i>
            </div>
            <h3>物业通知</h3>
            <p>及时获取停水停电、维修保养等重要通知公告</p>
            <div class="card-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>

          <div class="feature-card" @click="$router.push('/feedback')">
            <div class="card-icon service-icon">
              <i class="el-icon-service"></i>
            </div>
            <h3>在线客服</h3>
            <p>7x24小时在线服务，随时解答您的疑问</p>
            <div class="card-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </div>
      </section>

      <!-- 最新通知 -->
      <section class="notices-section">
        <div class="section-header">
          <h2>最新通知</h2>
          <router-link to="/notices" class="view-all">
            查看全部 <i class="el-icon-arrow-right"></i>
          </router-link>
        </div>
        <div class="notices-container" v-loading="loading">
          <div v-if="notices.length === 0" class="empty-state">
            <i class="el-icon-document"></i>
            <p>暂无通知公告</p>
          </div>
          <div v-else class="notice-cards">
            <div 
              v-for="notice in notices" 
              :key="notice.id" 
              class="notice-card"
              @click="$router.push('/notices')"
            >
              <div class="notice-tag" :class="getTagClass(notice.type)">
                {{ notice.type || '通知' }}
              </div>
              <h4 class="notice-title">{{ notice.title }}</h4>
              <p class="notice-preview">{{ notice.content }}</p>
              <div class="notice-meta">
                <span class="notice-date">
                  <i class="el-icon-time"></i>
                  {{ formatDate(notice.createdAt) }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 统计数据 -->
      <section class="stats-section">
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-number">24h</div>
            <div class="stat-label">响应时间</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">99%</div>
            <div class="stat-label">满意度</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">365</div>
            <div class="stat-label">全年服务</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">1000+</div>
            <div class="stat-label">服务业主</div>
          </div>
        </div>
      </section>
    </main>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-brand">
          <i class="el-icon-office-building"></i>
          <span>物业服务中心</span>
        </div>
        <div class="footer-links">
          <router-link to="/">首页</router-link>
          <router-link to="/feedback">反馈</router-link>
          <router-link to="/notices">通知</router-link>
          <router-link to="/admin/login">管理</router-link>
        </div>
        <div class="footer-copyright">
          © 2024 物业服务中心 · 用心服务每一位业主
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
import { noticeApi } from '@/api'

export default {
  name: 'Home',
  data() {
    return {
      notices: [],
      loading: false
    }
  },
  created() {
    this.fetchNotices()
  },
  methods: {
    async fetchNotices() {
      this.loading = true
      try {
        const res = await noticeApi.getAll()
        if (res.code === 200) {
          this.notices = res.data.slice(0, 3)
        }
      } catch (error) {
        console.error('获取通知失败', error)
      } finally {
        this.loading = false
      }
    },
    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleDateString('zh-CN', { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric' 
      })
    },
    getTagClass(type) {
      const classes = {
        '停水': 'tag-warning',
        '停电': 'tag-danger',
        '维修': 'tag-info',
        '其他': 'tag-default'
      }
      return classes[type] || 'tag-default'
    }
  }
}
</script>

<style scoped>
/* ========== 基础变量 ========== */
:root {
  --primary-color: #6366f1;
  --primary-dark: #4f46e5;
  --secondary-color: #8b5cf6;
  --accent-color: #f472b6;
  --text-primary: #1e293b;
  --text-secondary: #64748b;
  --text-muted: #94a3b8;
  --bg-primary: #f8fafc;
  --bg-white: #ffffff;
  --border-color: #e2e8f0;
  --shadow-sm: 0 1px 2px rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
  --shadow-xl: 0 20px 25px -5px rgba(0, 0, 0, 0.1);
  --radius-sm: 8px;
  --radius-md: 12px;
  --radius-lg: 16px;
  --radius-xl: 24px;
}

/* ========== 全局样式 ========== */
.home {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: var(--bg-primary);
}

/* ========== 导航栏 ========== */
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.nav-brand {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  font-weight: 600;
  color: var(--primary-color);
}

.nav-brand i {
  font-size: 24px;
}

.admin-link {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border-radius: var(--radius-sm);
  color: var(--text-secondary);
  text-decoration: none;
  font-size: 14px;
  transition: all 0.2s ease;
}

.admin-link:hover {
  background: var(--bg-primary);
  color: var(--primary-color);
}

/* ========== Hero区域 ========== */
.hero {
  position: relative;
  min-height: 520px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 100px 24px 80px;
  overflow: hidden;
}

.hero-bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 50%, #a855f7 100%);
  z-index: 0;
}

.hero-bg::before {
  content: '';
  position: absolute;
  inset: 0;
  background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.05'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
}

.hero-content {
  position: relative;
  z-index: 1;
  text-align: center;
  max-width: 600px;
}

.hero-title {
  margin-bottom: 20px;
}

.title-line {
  display: block;
  font-size: 48px;
  font-weight: 700;
  color: white;
  line-height: 1.2;
  letter-spacing: -1px;
}

.title-line.highlight {
  background: linear-gradient(90deg, #fef08a, #fde047);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 36px;
  letter-spacing: 2px;
}

.hero-buttons {
  display: flex;
  gap: 16px;
  justify-content: center;
  flex-wrap: wrap;
}

.btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 14px 32px;
  border-radius: 50px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.btn-primary {
  background: white;
  color: var(--primary-color);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
}

.btn-outline {
  background: transparent;
  border-color: rgba(255, 255, 255, 0.5);
  color: white;
}

.btn-outline:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: white;
}

.hero-wave {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 1;
}

.hero-wave svg {
  display: block;
  width: 100%;
  height: 60px;
}

/* ========== 主要内容 ========== */
.main-content {
  flex: 1;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  width: 100%;
}

.section-header {
  text-align: center;
  margin-bottom: 40px;
}

.section-header h2 {
  font-size: 32px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 12px;
}

.section-header p {
  font-size: 16px;
  color: var(--text-secondary);
}

.section-header .view-all {
  color: var(--primary-color);
  text-decoration: none;
  font-size: 14px;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  transition: gap 0.2s ease;
}

.section-header .view-all:hover {
  gap: 8px;
}

/* ========== 功能卡片 ========== */
.features {
  padding: 60px 0;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.feature-card {
  background: var(--bg-white);
  border-radius: var(--radius-lg);
  padding: 32px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid var(--border-color);
  position: relative;
  overflow: hidden;
}

.feature-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, var(--primary-color), var(--secondary-color));
  opacity: 0;
  transition: opacity 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: var(--shadow-xl);
  border-color: transparent;
}

.feature-card:hover::before {
  opacity: 1;
}

.card-icon {
  width: 64px;
  height: 64px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.card-icon i {
  font-size: 28px;
  color: white;
}

.feedback-icon {
  background: linear-gradient(135deg, #6366f1, #8b5cf6);
}

.notice-icon {
  background: linear-gradient(135deg, #f472b6, #ec4899);
}

.service-icon {
  background: linear-gradient(135deg, #14b8a6, #06b6d4);
}

.feature-card h3 {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 12px;
}

.feature-card p {
  font-size: 14px;
  color: var(--text-secondary);
  line-height: 1.6;
  margin-bottom: 16px;
}

.card-arrow {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: var(--bg-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-muted);
  transition: all 0.3s ease;
}

.feature-card:hover .card-arrow {
  background: var(--primary-color);
  color: white;
  transform: translateX(4px);
}

/* ========== 通知区域 ========== */
.notices-section {
  padding: 60px 0;
}

.notices-section .section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  text-align: left;
  margin-bottom: 32px;
}

.notices-section .section-header h2 {
  margin-bottom: 0;
}

.notices-container {
  min-height: 200px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: var(--text-muted);
}

.empty-state i {
  font-size: 48px;
  margin-bottom: 16px;
}

.notice-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.notice-card {
  background: var(--bg-white);
  border-radius: var(--radius-lg);
  padding: 24px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid var(--border-color);
}

.notice-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
  border-color: var(--primary-color);
}

.notice-tag {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
  margin-bottom: 12px;
}

.tag-warning {
  background: #fef3c7;
  color: #d97706;
}

.tag-danger {
  background: #fee2e2;
  color: #dc2626;
}

.tag-info {
  background: #dbeafe;
  color: #2563eb;
}

.tag-default {
  background: #f1f5f9;
  color: #64748b;
}

.notice-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 8px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.notice-preview {
  font-size: 14px;
  color: var(--text-secondary);
  line-height: 1.6;
  margin-bottom: 16px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.notice-meta {
  display: flex;
  align-items: center;
}

.notice-date {
  font-size: 13px;
  color: var(--text-muted);
  display: flex;
  align-items: center;
  gap: 4px;
}

/* ========== 统计数据 ========== */
.stats-section {
  padding: 60px 0 80px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  background: linear-gradient(135deg, #6366f1, #8b5cf6);
  border-radius: var(--radius-xl);
  padding: 48px 32px;
}

.stat-item {
  text-align: center;
  color: white;
}

.stat-number {
  font-size: 42px;
  font-weight: 700;
  margin-bottom: 8px;
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
}

/* ========== 页脚 ========== */
.footer {
  background: var(--text-primary);
  color: white;
  padding: 48px 24px;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
}

.footer-brand {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 20px;
  font-weight: 600;
}

.footer-brand i {
  font-size: 24px;
}

.footer-links {
  display: flex;
  gap: 32px;
}

.footer-links a {
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  font-size: 14px;
  transition: color 0.2s ease;
}

.footer-links a:hover {
  color: white;
}

.footer-copyright {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.5);
}

/* ========== 响应式设计 ========== */

/* 平板设备 (768px - 1024px) */
@media (max-width: 1024px) {
  .feature-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .notice-cards {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 32px;
  }
  
  .title-line {
    font-size: 40px;
  }
}

/* 小平板/大手机 (576px - 768px) */
@media (max-width: 768px) {
  .navbar {
    background: rgba(255, 255, 255, 0.95);
  }
  
  .nav-container {
    padding: 0 16px;
    height: 56px;
  }
  
  .nav-brand span {
    display: none;
  }
  
  .admin-link span {
    display: none;
  }
  
  .hero {
    min-height: 450px;
    padding: 80px 20px 60px;
  }
  
  .title-line {
    font-size: 32px;
  }
  
  .hero-subtitle {
    font-size: 15px;
    letter-spacing: 1px;
  }
  
  .hero-buttons {
    flex-direction: column;
    align-items: center;
  }
  
  .btn {
    width: 100%;
    max-width: 280px;
    justify-content: center;
  }
  
  .main-content {
    padding: 0 16px;
  }
  
  .section-header h2 {
    font-size: 26px;
  }
  
  .features {
    padding: 40px 0;
  }
  
  .feature-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  
  .feature-card {
    padding: 24px;
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 20px;
  }
  
  .feature-card .card-icon {
    margin-bottom: 0;
    flex-shrink: 0;
  }
  
  .feature-card h3 {
    margin-bottom: 4px;
  }
  
  .feature-card p {
    margin-bottom: 0;
    font-size: 13px;
  }
  
  .card-arrow {
    display: none;
  }
  
  .notices-section {
    padding: 40px 0;
  }
  
  .notices-section .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .notice-cards {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  
  .stats-section {
    padding: 40px 0 60px;
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
    padding: 32px 24px;
    gap: 24px;
  }
  
  .stat-number {
    font-size: 32px;
  }
  
  .stat-label {
    font-size: 13px;
  }
  
  .footer {
    padding: 32px 16px;
  }
  
  .footer-links {
    gap: 20px;
    flex-wrap: wrap;
    justify-content: center;
  }
}

/* 手机设备 (< 576px) */
@media (max-width: 576px) {
  .hero {
    min-height: 400px;
    padding: 70px 16px 50px;
  }
  
  .title-line {
    font-size: 28px;
  }
  
  .hero-subtitle {
    font-size: 14px;
    margin-bottom: 28px;
  }
  
  .btn {
    padding: 12px 24px;
    font-size: 15px;
  }
  
  .section-header {
    margin-bottom: 24px;
  }
  
  .section-header h2 {
    font-size: 22px;
  }
  
  .section-header p {
    font-size: 14px;
  }
  
  .feature-card {
    padding: 20px;
    gap: 16px;
  }
  
  .card-icon {
    width: 52px;
    height: 52px;
  }
  
  .card-icon i {
    font-size: 24px;
  }
  
  .feature-card h3 {
    font-size: 16px;
  }
  
  .notice-card {
    padding: 20px;
  }
  
  .notice-title {
    font-size: 15px;
  }
  
  .stats-grid {
    padding: 28px 20px;
    border-radius: var(--radius-lg);
  }
  
  .stat-number {
    font-size: 28px;
  }
  
  .footer-brand {
    font-size: 18px;
  }
  
  .footer-links {
    gap: 16px;
  }
  
  .footer-links a {
    font-size: 13px;
  }
}

/* 超小屏幕 (< 375px) */
@media (max-width: 375px) {
  .title-line {
    font-size: 24px;
  }
  
  .hero-subtitle {
    font-size: 13px;
  }
  
  .feature-card {
    flex-direction: column;
    text-align: center;
  }
  
  .stats-grid {
    grid-template-columns: 1fr 1fr;
    gap: 20px;
  }
  
  .stat-number {
    font-size: 24px;
  }
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.hero-content {
  animation: fadeInUp 0.8s ease-out;
}

.feature-card {
  animation: fadeInUp 0.6s ease-out;
  animation-fill-mode: both;
}

.feature-card:nth-child(1) { animation-delay: 0.1s; }
.feature-card:nth-child(2) { animation-delay: 0.2s; }
.feature-card:nth-child(3) { animation-delay: 0.3s; }

.notice-card {
  animation: fadeInUp 0.6s ease-out;
  animation-fill-mode: both;
}

.notice-card:nth-child(1) { animation-delay: 0.1s; }
.notice-card:nth-child(2) { animation-delay: 0.2s; }
.notice-card:nth-child(3) { animation-delay: 0.3s; }
</style>
