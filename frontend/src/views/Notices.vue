<template>
  <div class="notices-page">
    <header class="header">
      <div class="container">
        <router-link to="/" class="back">
          <i class="el-icon-arrow-left"></i> 返回首页
        </router-link>
        <h1>物业通知</h1>
      </div>
    </header>

    <main class="main container">
      <div class="content" v-loading="loading">
        <div v-if="notices.length === 0" class="empty">
          <i class="el-icon-bell"></i>
          <p>暂无通知公告</p>
        </div>

        <div v-for="notice in notices" :key="notice.id" class="notice-card" @click="showDetail(notice)">
          <div class="card-header">
            <el-tag :type="getTagType(notice.type)" size="small">{{ notice.type || '通知' }}</el-tag>
            <span class="date">{{ formatDate(notice.createdAt) }}</span>
          </div>
          <h3 class="card-title">{{ notice.title }}</h3>
          <p class="card-content">{{ notice.content }}</p>
        </div>
      </div>
    </main>

    <el-dialog :title="currentNotice.title" :visible.sync="dialogVisible" width="600px">
      <div class="dialog-content">
        <div class="dialog-meta">
          <el-tag :type="getTagType(currentNotice.type)" size="small">{{ currentNotice.type || '通知' }}</el-tag>
          <span>发布时间：{{ formatDate(currentNotice.createdAt) }}</span>
        </div>
        <div class="dialog-text">{{ currentNotice.content }}</div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { noticeApi } from '@/api'

export default {
  name: 'Notices',
  data() {
    return {
      notices: [],
      loading: false,
      dialogVisible: false,
      currentNotice: {}
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
          this.notices = res.data
        }
      } catch (error) {
        this.$message.error('获取通知列表失败')
      } finally {
        this.loading = false
      }
    },
    showDetail(notice) {
      this.currentNotice = notice
      this.dialogVisible = true
    },
    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleString('zh-CN')
    },
    getTagType(type) {
      const types = {
        '停水': 'warning',
        '停电': 'danger',
        '维修': 'info',
        '其他': ''
      }
      return types[type] || ''
    }
  }
}
</script>

<style scoped>
.notices-page {
  min-height: 100vh;
  background: #f5f7fa;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 30px 0;
}

.container {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 20px;
}

.back {
  color: white;
  text-decoration: none;
  font-size: 14px;
  display: inline-block;
  margin-bottom: 15px;
}

.back:hover {
  opacity: 0.8;
}

.header h1 {
  font-size: 28px;
}

.main {
  padding: 30px 20px;
}

.content {
  min-height: 300px;
}

.empty {
  background: white;
  border-radius: 12px;
  text-align: center;
  padding: 80px 20px;
  color: #999;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.empty i {
  font-size: 56px;
  margin-bottom: 20px;
}

.notice-card {
  background: white;
  border-radius: 12px;
  padding: 25px;
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.notice-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.date {
  color: #999;
  font-size: 13px;
}

.card-title {
  font-size: 18px;
  color: #333;
  margin-bottom: 10px;
}

.card-content {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.dialog-content {
  padding: 10px 0;
}

.dialog-meta {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
  color: #999;
  font-size: 13px;
}

.dialog-text {
  color: #333;
  line-height: 1.8;
  white-space: pre-wrap;
}
</style>
