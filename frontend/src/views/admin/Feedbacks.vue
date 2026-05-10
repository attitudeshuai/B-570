<template>
  <div class="feedbacks-page">
    <div class="page-header">
      <h2>反馈管理</h2>
    </div>

    <el-card>
      <el-table :data="feedbacks" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="70"></el-table-column>
        <el-table-column prop="contactName" label="联系人" width="100">
          <template slot-scope="scope">
            {{ scope.row.contactName || '匿名' }}
          </template>
        </el-table-column>
        <el-table-column prop="contact" label="联系方式" width="140">
          <template slot-scope="scope">
            {{ scope.row.contact || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="content" label="反馈内容" min-width="200" show-overflow-tooltip>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="提交时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleReply(scope.row)">
              处理
            </el-button>
            <el-button type="text" size="small" style="color: #F56C6C" @click="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="处理反馈" :visible.sync="dialogVisible" width="550px">
      <el-form ref="replyForm" :model="replyForm" label-width="80px">
        <el-form-item label="反馈内容">
          <div class="feedback-content">{{ currentFeedback.content }}</div>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="replyForm.status">
            <el-radio label="pending">待处理</el-radio>
            <el-radio label="processing">处理中</el-radio>
            <el-radio label="resolved">已解决</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="回复内容">
          <el-input 
            type="textarea" 
            v-model="replyForm.reply" 
            :rows="4"
            placeholder="请输入回复内容..."
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReply" :loading="submitting">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { feedbackApi } from '@/api'

export default {
  name: 'AdminFeedbacks',
  data() {
    return {
      feedbacks: [],
      loading: false,
      dialogVisible: false,
      currentFeedback: {},
      replyForm: {
        status: 'pending',
        reply: ''
      },
      submitting: false
    }
  },
  created() {
    this.fetchFeedbacks()
  },
  methods: {
    async fetchFeedbacks() {
      this.loading = true
      try {
        const res = await feedbackApi.getAll()
        if (res.code === 200) {
          this.feedbacks = res.data
        }
      } catch (error) {
        this.$message.error('获取反馈列表失败')
      } finally {
        this.loading = false
      }
    },
    handleReply(row) {
      this.currentFeedback = row
      this.replyForm = {
        status: row.status || 'pending',
        reply: row.reply || ''
      }
      this.dialogVisible = true
    },
    async submitReply() {
      this.submitting = true
      try {
        const res = await feedbackApi.reply(this.currentFeedback.id, this.replyForm)
        if (res.code === 200) {
          this.$message.success('处理成功')
          this.dialogVisible = false
          this.fetchFeedbacks()
        } else {
          this.$message.error(res.message || '处理失败')
        }
      } catch (error) {
        this.$message.error('处理失败')
      } finally {
        this.submitting = false
      }
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确定删除该反馈吗？', '提示', {
          type: 'warning'
        })
        const res = await feedbackApi.delete(row.id)
        if (res.code === 200) {
          this.$message.success('删除成功')
          this.fetchFeedbacks()
        } else {
          this.$message.error(res.message || '删除失败')
        }
      } catch (e) {
        // cancelled
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
.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #333;
  font-size: 20px;
}

.feedback-content {
  background: #f5f7fa;
  padding: 12px 15px;
  border-radius: 6px;
  color: #666;
  line-height: 1.6;
}
</style>
