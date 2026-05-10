<template>
  <div class="login-page">
    <div class="login-box">
      <h2>管理员登录</h2>
      <el-form ref="form" :model="form" :rules="rules" @submit.native.prevent="handleLogin">
        <el-form-item prop="username">
          <el-input 
            v-model="form.username" 
            prefix-icon="el-icon-user"
            placeholder="用户名"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="form.password" 
            type="password"
            prefix-icon="el-icon-lock"
            placeholder="密码"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button 
            type="primary" 
            style="width: 100%"
            :loading="loading"
            @click="handleLogin"
          >
            登 录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="back-link">
        <router-link to="/">
          <i class="el-icon-arrow-left"></i> 返回首页
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { authApi } from '@/api'

export default {
  name: 'AdminLogin',
  data() {
    return {
      form: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      },
      loading: false
    }
  },
  methods: {
    async handleLogin() {
      try {
        await this.$refs.form.validate()
        this.loading = true
        const res = await authApi.login(this.form)
        if (res.code === 200) {
          localStorage.setItem('admin_token', res.data.token)
          localStorage.setItem('admin_user', JSON.stringify(res.data))
          this.$message.success('登录成功')
          this.$router.push('/admin')
        } else {
          this.$message.error(res.message || '登录失败')
        }
      } catch (error) {
        if (error !== false) {
          this.$message.error('登录失败，请检查用户名和密码')
        }
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 380px;
  background: white;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
}

.login-box h2 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
  font-size: 24px;
}

.back-link {
  text-align: center;
  margin-top: 20px;
}

.back-link a {
  color: #667eea;
  text-decoration: none;
  font-size: 14px;
}

.back-link a:hover {
  text-decoration: underline;
}
</style>
