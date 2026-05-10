<template>
  <div class="notices-page">
    <div class="page-header">
      <h2>通知管理</h2>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
        发布通知
      </el-button>
    </div>

    <el-card>
      <el-table :data="notices" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="70"></el-table-column>
        <el-table-column prop="type" label="类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getTagType(scope.row.type)" size="small">
              {{ scope.row.type || '通知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip>
        </el-table-column>
        <el-table-column prop="content" label="内容" min-width="250" show-overflow-tooltip>
        </el-table-column>
        <el-table-column prop="createdAt" label="发布时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button type="text" size="small" style="color: #F56C6C" @click="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="550px">
      <el-form ref="noticeForm" :model="noticeForm" :rules="rules" label-width="80px">
        <el-form-item label="类型" prop="type">
          <el-select v-model="noticeForm.type" placeholder="请选择类型" style="width: 100%">
            <el-option label="停水" value="停水"></el-option>
            <el-option label="停电" value="停电"></el-option>
            <el-option label="维修" value="维修"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="noticeForm.title" placeholder="请输入通知标题"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input 
            type="textarea" 
            v-model="noticeForm.content" 
            :rows="6"
            placeholder="请输入通知内容..."
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitNotice" :loading="submitting">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { noticeApi } from '@/api'

export default {
  name: 'AdminNotices',
  data() {
    return {
      notices: [],
      loading: false,
      dialogVisible: false,
      dialogTitle: '发布通知',
      editingId: null,
      noticeForm: {
        type: '',
        title: '',
        content: ''
      },
      rules: {
        type: [
          { required: true, message: '请选择通知类型', trigger: 'change' }
        ],
        title: [
          { required: true, message: '请输入通知标题', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '请输入通知内容', trigger: 'blur' }
        ]
      },
      submitting: false
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
    handleAdd() {
      this.dialogTitle = '发布通知'
      this.editingId = null
      this.noticeForm = {
        type: '',
        title: '',
        content: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑通知'
      this.editingId = row.id
      this.noticeForm = {
        type: row.type || '',
        title: row.title,
        content: row.content
      }
      this.dialogVisible = true
    },
    async submitNotice() {
      try {
        await this.$refs.noticeForm.validate()
        this.submitting = true
        
        let res
        if (this.editingId) {
          res = await noticeApi.update(this.editingId, this.noticeForm)
        } else {
          res = await noticeApi.create(this.noticeForm)
        }
        
        if (res.code === 200) {
          this.$message.success(this.editingId ? '更新成功' : '发布成功')
          this.dialogVisible = false
          this.fetchNotices()
        } else {
          this.$message.error(res.message || '操作失败')
        }
      } catch (error) {
        if (error !== false) {
          this.$message.error('操作失败')
        }
      } finally {
        this.submitting = false
      }
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确定删除该通知吗？', '提示', {
          type: 'warning'
        })
        const res = await noticeApi.delete(row.id)
        if (res.code === 200) {
          this.$message.success('删除成功')
          this.fetchNotices()
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
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #333;
  font-size: 20px;
}
</style>
