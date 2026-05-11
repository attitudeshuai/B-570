<template>
  <div class="rating-statistics-page">
    <div class="page-header">
      <h2>满意度统计</h2>
    </div>

    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm">
        <el-form-item label="开始时间">
          <el-date-picker
            v-model="filterForm.startDate"
            type="date"
            placeholder="选择开始日期"
            value-format="yyyy-MM-dd"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker
            v-model="filterForm.endDate"
            type="date"
            placeholder="选择结束日期"
            value-format="yyyy-MM-dd"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchStatistics" :loading="loading">查询</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-table :data="statisticsList" v-loading="loading" stripe border>
      <el-table-column prop="type" label="反馈类型" width="150">
        <template slot-scope="scope">
          {{ scope.row.type || '未分类' }}
        </template>
      </el-table-column>
      <el-table-column prop="ratedCount" label="评价数" width="120"></el-table-column>
      <el-table-column prop="averageScore" label="平均分" width="120">
        <template slot-scope="scope">
          <span style="color: #F7BA2A; font-weight: 600;">{{ scope.row.averageScore || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="badRate" label="差评率" width="120">
        <template slot-scope="scope">
          <span style="color: #F56C6C; font-weight: 600;">{{ scope.row.badRate || 0 }}%</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { feedbackApi } from '@/api'

export default {
  name: 'RatingStatistics',
  data() {
    return {
      filterForm: {
        startDate: '',
        endDate: ''
      },
      statisticsList: [],
      loading: false
    }
  },
  created() {
    this.initDefaultDate()
    this.fetchStatistics()
  },
  methods: {
    initDefaultDate() {
      const end = new Date()
      const start = new Date()
      start.setMonth(start.getMonth() - 1)
      this.filterForm.startDate = this.formatDate(start)
      this.filterForm.endDate = this.formatDate(end)
    },
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    async fetchStatistics() {
      if (!this.filterForm.startDate || !this.filterForm.endDate) {
        this.$message.warning('请选择时间范围')
        return
      }
      this.loading = true
      try {
        const res = await feedbackApi.getRatingStatistics({
          startTime: `${this.filterForm.startDate}T00:00:00`,
          endTime: `${this.filterForm.endDate}T23:59:59`
        })
        if (res.code === 200) {
          this.statisticsList = res.data || []
        }
      } catch (error) {
        this.$message.error('获取统计数据失败')
      } finally {
        this.loading = false
      }
    },
    resetFilter() {
      this.initDefaultDate()
      this.fetchStatistics()
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

.filter-card {
  margin-bottom: 20px;
}
</style>
