<template>
  <div class="statistics-page">
    <div class="page-header">
      <h2>评分统计</h2>
    </div>

    <el-card>
      <el-form :inline="true" class="filter-form">
        <el-form-item label="开始时间">
          <el-date-picker
            v-model="filter.startTime"
            type="datetime"
            placeholder="选择开始时间"
            value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker
            v-model="filter.endTime"
            type="datetime"
            placeholder="选择结束时间"
            value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchStatistics">查询</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="statistics" v-loading="loading" stripe>
        <el-table-column prop="feedbackType" label="反馈类型" width="120">
          <template slot-scope="scope">
            {{ scope.row.feedbackType || '未分类' }}
          </template>
        </el-table-column>
        <el-table-column prop="totalCount" label="评分总数" width="100">
        </el-table-column>
        <el-table-column prop="averageRating" label="平均分" width="120">
          <template slot-scope="scope">
            <span :style="{ color: getRatingColor(scope.row.averageRating) }">
              {{ scope.row.averageRating ? scope.row.averageRating.toFixed(2) : '-' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="negativeCount" label="差评数" width="100">
        </el-table-column>
        <el-table-column prop="negativeRate" label="差评率" width="120">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.negativeRate ? Math.round(scope.row.negativeRate) : 0"
              :status="getProgressStatus(scope.row.negativeRate)"
              :stroke-width="10">
            </el-progress>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="statistics.length === 0 && !loading" class="empty">
        <i class="el-icon-data-line"></i>
        <p>暂无统计数据</p>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ratingApi } from '@/api'

export default {
  name: 'RatingStatistics',
  data() {
    return {
      filter: {
        startTime: '',
        endTime: ''
      },
      statistics: [],
      loading: false
    }
  },
  created() {
    this.fetchStatistics()
  },
  methods: {
    async fetchStatistics() {
      this.loading = true
      try {
        const params = {}
        if (this.filter.startTime) {
          params.startTime = this.filter.startTime
        }
        if (this.filter.endTime) {
          params.endTime = this.filter.endTime
        }
        const res = await ratingApi.getStatistics(params)
        if (res.code === 200) {
          this.statistics = res.data
        }
      } catch (error) {
        this.$message.error('获取统计数据失败')
      } finally {
        this.loading = false
      }
    },
    resetFilter() {
      this.filter = {
        startTime: '',
        endTime: ''
      }
      this.fetchStatistics()
    },
    getRatingColor(rating) {
      if (!rating) return '#999'
      if (rating >= 4) return '#67C23A'
      if (rating >= 3) return '#E6A23C'
      return '#F56C6C'
    },
    getProgressStatus(rate) {
      if (!rate) return null
      if (rate >= 30) return 'exception'
      if (rate >= 15) return 'warning'
      return 'success'
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

.filter-form {
  margin-bottom: 20px;
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
</style>
