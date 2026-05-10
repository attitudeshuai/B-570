<template>
  <div class="feedback-page">
    <header class="header">
      <div class="container">
        <router-link to="/" class="back">
          <i class="el-icon-arrow-left"></i> 返回首页
        </router-link>
        <h1>意见反馈</h1>
      </div>
    </header>

    <main class="main container">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="提交反馈" name="submit">
          <div class="form-section">
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
              <el-form-item label="反馈类型" prop="type">
                <el-select v-model="form.type" placeholder="请选择反馈类型" style="width: 100%">
                  <el-option label="维修" value="维修"></el-option>
                  <el-option label="建议" value="建议"></el-option>
                  <el-option label="投诉" value="投诉"></el-option>
                  <el-option label="安全" value="安全"></el-option>
                  <el-option label="其他" value="其他"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="联系人" prop="contactName">
                <el-input v-model="form.contactName" placeholder="请输入您的姓名（选填）"></el-input>
              </el-form-item>
              <el-form-item label="联系方式" prop="contact">
                <el-input v-model="form.contact" placeholder="请输入手机号或邮箱（选填）"></el-input>
              </el-form-item>
              <el-form-item label="反馈内容" prop="content">
                <el-input
                  type="textarea"
                  v-model="form.content"
                  :rows="6"
                  placeholder="请详细描述您的问题、建议或意见..."
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitFeedback" :loading="submitting">
                  提交反馈
                </el-button>
                <el-button @click="resetForm">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>

        <el-tab-pane label="我的反馈" name="list">
          <div class="list-section" v-loading="loading">
            <div v-if="feedbacks.length === 0" class="empty">
              <i class="el-icon-document"></i>
              <p>暂无反馈记录</p>
            </div>
            <div v-for="item in feedbacks" :key="item.id" class="feedback-item">
              <div class="item-header">
                <div>
                  <el-tag :type="getStatusType(item.status)" size="small">
                    {{ getStatusText(item.status) }}
                  </el-tag>
                  <el-tag v-if="item.type" type="info" size="small" style="margin-left: 8px">
                    {{ item.type }}
                  </el-tag>
                </div>
                <span class="date">{{ formatDate(item.createdAt) }}</span>
              </div>
              <div class="item-content">{{ item.content }}</div>
              <div v-if="item.reply" class="item-reply">
                <strong>物业回复：</strong>{{ item.reply }}
              </div>
              <div v-if="item.status === 'resolved'" class="item-rating">
                <template v-if="item.rating">
                  <span class="rated-text">您的评分：</span>
                  <el-rate v-model="item.rating" disabled text-color="#ff9900"></el-rate>
                  <span class="rated-note">（已锁定）</span>
                </template>
                <template v-else>
                  <span class="rating-text">请为本次服务评分：</span>
                  <el-rate v-model="item.tempRating" @change="handleRateChange(item, $event)"></el-rate>
                </template>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </main>
  </div>
</template>

<script>
import { feedbackApi, ratingApi } from '@/api'

export default {
  name: 'Feedback',
  data() {
    return {
      activeTab: 'submit',
      form: {
        contactName: '',
        contact: '',
        content: '',
        type: ''
      },
      rules: {
        type: [
          { required: true, message: '请选择反馈类型', trigger: 'change' }
        ],
        content: [
          { required: true, message: '请输入反馈内容', trigger: 'blur' },
          { min: 10, message: '反馈内容至少10个字符', trigger: 'blur' }
        ]
      },
      feedbacks: [],
      loading: false,
      submitting: false
    }
  },
  watch: {
    activeTab(val) {
      if (val === 'list') {
        this.fetchFeedbacks()
      }
    }
  },
  methods: {
    async submitFeedback() {
      try {
        await this.$refs.form.validate()
        this.submitting = true
        const res = await feedbackApi.create(this.form)
        if (res.code === 200) {
          this.$message.success('反馈提交成功！')
          this.resetForm()
          this.activeTab = 'list'
        } else {
          this.$message.error(res.message || '提交失败')
        }
      } catch (error) {
        if (error !== false) {
          this.$message.error('提交失败，请稍后重试')
        }
      } finally {
        this.submitting = false
      }
    },
    resetForm() {
      this.$refs.form.resetFields()
    },
    async fetchFeedbacks() {
      this.loading = true
      try {
        const res = await feedbackApi.getAll()
        if (res.code === 200) {
          this.feedbacks = res.data.map(item => ({
            ...item,
            tempRating: 0
          }))
        }
      } catch (error) {
        console.error('获取反馈列表失败', error)
      } finally {
        this.loading = false
      }
    },
    async handleRateChange(item, rating) {
      try {
        const res = await ratingApi.create({
          feedbackId: item.id,
          rating: rating
        })
        if (res.code === 200) {
          this.$message.success('评分成功！')
          item.rating = rating
        } else {
          this.$message.error(res.message || '评分失败')
          item.tempRating = 0
        }
      } catch (error) {
        this.$message.error('评分失败，请稍后重试')
        item.tempRating = 0
      }
    },
    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleString('zh-CN')
    },
    getStatusType(status) {
      const types = {
        'pending': 'info',
        'processing': 'warning',
        'resolved': 'success'
      }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = {
        'pending': '待处理',
        'processing': '处理中',
        'resolved': '已解决'
      }
      return texts[status] || '待处理'
    }
  }
}
</script>

<style scoped>
.feedback-page {
  min-height: 100vh;
  background: #f5f7fa;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 30px 0;
}

.container {
  max-width: 800px;
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

.form-section, .list-section {
  background: white;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.empty {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.empty i {
  font-size: 48px;
  margin-bottom: 15px;
}

.feedback-item {
  padding: 20px;
  border: 1px solid #eee;
  border-radius: 8px;
  margin-bottom: 15px;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.date {
  color: #999;
  font-size: 13px;
}

.item-content {
  color: #333;
  line-height: 1.6;
  margin-bottom: 12px;
}

.item-reply {
  background: #f5f7fa;
  padding: 12px 15px;
  border-radius: 6px;
  color: #666;
  font-size: 14px;
}

.item-reply strong {
  color: #667eea;
}

.item-rating {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px dashed #eee;
  display: flex;
  align-items: center;
}

.rating-text, .rated-text {
  color: #666;
  font-size: 14px;
  margin-right: 8px;
}

.rated-note {
  color: #999;
  font-size: 12px;
  margin-left: 8px;
}
</style>
