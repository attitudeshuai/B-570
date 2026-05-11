import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/feedback',
    name: 'Feedback',
    component: () => import('../views/Feedback.vue')
  },
  {
    path: '/notices',
    name: 'Notices',
    component: () => import('../views/Notices.vue')
  },
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: () => import('../views/admin/Login.vue')
  },
  {
    path: '/admin',
    name: 'Admin',
    component: () => import('../views/admin/Layout.vue'),
    redirect: '/admin/feedbacks',
    children: [
      {
        path: 'feedbacks',
        name: 'AdminFeedbacks',
        component: () => import('../views/admin/Feedbacks.vue')
      },
      {
        path: 'notices',
        name: 'AdminNotices',
        component: () => import('../views/admin/Notices.vue')
      },
      {
        path: 'rating-statistics',
        name: 'RatingStatistics',
        component: () => import('../views/admin/RatingStatistics.vue')
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  if (to.path.startsWith('/admin') && to.path !== '/admin/login') {
    const isLoggedIn = localStorage.getItem('admin_token')
    if (!isLoggedIn) {
      next('/admin/login')
      return
    }
  }
  next()
})

export default router
