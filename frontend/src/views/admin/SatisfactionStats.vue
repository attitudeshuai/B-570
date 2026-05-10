<template>
  <div class="stats-page">
    <div class="page-header">
      <h2>满意度统计</h2>
    </div>

    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="开始日期">
          <el-date-picker
            v-model="filterForm.startDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择开始日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker
            v-model="filterForm.endDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择结束日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchStats" icon="el-icon-search">查询</el-button>
          <el-button @click="resetFilter" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-loading="loading">
      <div v-if="stats.length === 0 && !loading" class="empty">
        <i class="el-icon-data-analysis"></i>
        <p>暂无评分数据</p>
      </div>
      <el-table v-else :data="stats" stripe border>
        <el-table-column prop="type" label="反馈类型" width="150">
          <template slot-scope="scope">
            <el-tag size="small">{{ scope.row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="total_count" label="评价数" width="100" align="center"></el-table-column>
        <el-table-column prop="avg_score" label="平均分" width="120" align="center">
          <template slot-scope="scope">
            <span :class="getScoreClass(scope.row.avg_score)">{{ scope.row.avg_score }}</span>
          </template>
        </el-table-column>
        <el-table-column label="平均评分" width="200" align="center">
          <template slot-scope="scope">
            <el-rate
              :value="Number(scope.row.avg_score)"
              disabled
              show-score
              text-color="#ff9900"
            ></el-rate>
          </template>
        </el-table-column>
        <el-table-column prop="bad_count" label="差评数" width="100" align="center">
          <template slot-scope="scope">
            <span style="color: #F56C6C; font-weight: bold;">{{ scope.row.bad_count }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="bad_rate" label="差评率" width="120" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="Number(scope.row.bad_rate)"
              :color="getBadRateColor(Number(scope.row.bad_rate))"
              :stroke-width="16"
              :text-inside="true"
            ></el-progress>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { ratingApi } from '@/api'

export default {
  name: 'SatisfactionStats',
  data() {
    return {
      filterForm: {
        startDate: '',
        endDate: ''
      },
      stats: [],
      loading: false
    }
  },
  created() {
    this.fetchStats()
  },
  methods: {
    async fetchStats() {
      this.loading = true
      try {
        const params = {}
        if (this.filterForm.startDate) params.startDate = this.filterForm.startDate
        if (this.filterForm.endDate) params.endDate = this.filterForm.endDate + ' 23:59:59'
        const res = await ratingApi.getStats(params)
        if (res.code === 200) {
          this.stats = res.data || []
        }
      } catch (error) {
        this.$message.error('获取统计数据失败')
      } finally {
        this.loading = false
      }
    },
    resetFilter() {
      this.filterForm = { startDate: '', endDate: '' }
      this.fetchStats()
    },
    getScoreClass(score) {
      const s = Number(score)
      if (s >= 4) return 'score-good'
      if (s >= 3) return 'score-medium'
      return 'score-bad'
    },
    getBadRateColor(rate) {
      if (rate >= 50) return '#F56C6C'
      if (rate >= 30) return '#E6A23C'
      return '#67C23A'
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

.filter-form {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
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

.score-good {
  color: #67C23A;
  font-weight: bold;
  font-size: 16px;
}

.score-medium {
  color: #E6A23C;
  font-weight: bold;
  font-size: 16px;
}

.score-bad {
  color: #F56C6C;
  font-weight: bold;
  font-size: 16px;
}
</style>
