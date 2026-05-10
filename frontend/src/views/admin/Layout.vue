<template>
  <div class="admin-layout">
    <el-container>
      <el-aside width="220px">
        <div class="logo">
          <h2>物业管理系统</h2>
        </div>
        <el-menu
          :default-active="$route.path"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
        >
          <el-menu-item index="/admin/feedbacks">
            <i class="el-icon-message"></i>
            <span>反馈管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/notices">
            <i class="el-icon-bell"></i>
            <span>通知管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-container>
        <el-header>
          <div class="header-right">
            <span class="username">
              <i class="el-icon-user"></i>
              {{ adminUser.username || '管理员' }}
            </span>
            <el-button type="text" @click="handleLogout">
              <i class="el-icon-switch-button"></i> 退出
            </el-button>
          </div>
        </el-header>
        
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { authApi } from '@/api'

export default {
  name: 'AdminLayout',
  data() {
    return {
      adminUser: {}
    }
  },
  created() {
    const user = localStorage.getItem('admin_user')
    if (user) {
      this.adminUser = JSON.parse(user)
    }
  },
  methods: {
    async handleLogout() {
      try {
        await authApi.logout()
      } catch (e) {
        // ignore
      }
      localStorage.removeItem('admin_token')
      localStorage.removeItem('admin_user')
      this.$message.success('已退出登录')
      this.$router.push('/admin/login')
    }
  }
}
</script>

<style scoped>
.admin-layout {
  min-height: 100vh;
}

.el-container {
  min-height: 100vh;
}

.el-aside {
  background-color: #304156;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #263445;
}

.logo h2 {
  color: #fff;
  font-size: 16px;
  margin: 0;
}

.el-menu {
  border-right: none;
}

.el-header {
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.username {
  color: #666;
  font-size: 14px;
}

.el-main {
  background-color: #f5f7fa;
  padding: 20px;
}
</style>
