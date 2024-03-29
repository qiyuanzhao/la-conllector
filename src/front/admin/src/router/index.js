import Vue from 'vue'
import Router from 'vue-router'
const _import = require('./_import_' + process.env.NODE_ENV)
// in development env not use Lazy Loading,because Lazy Loading too many pages will cause webpack hot update too slow.so only in production use Lazy Loading

/* layout */
import Layout from '../views/layout/Layout'

Vue.use(Router)

 /**
  * icon : the icon show in the sidebar
  * hidden : if `hidden:true` will not show in the sidebar
  * redirect : if `redirect:noredirect` will not redirct in the levelbar
  * noDropdown : if `noDropdown:true` will not has submenu in the sidebar
  * meta : `{ role: ['admin'] }`  will control the page role
  **/
export const constantRouterMap = [
  { path: '/login', component: _import('login/index'), hidden: true },
  { path: '/404', component: _import('404'), hidden: true },
  {
    path: '/',
    component: Layout,
    redirect: '/now/index',
    icon: 'el-icon-document',
    noDropdown: true,
    children: []
  }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

export const asyncRouterMap = [
  {
    path: '/now',
    component: Layout,
    redirect: '/now/index',
    icon: 'el-icon-document',
    noDropdown: true,
    children: [{ path: 'index', name: '今日推送', component: _import('now/index'), meta: { role: ['admin'] }}]
  },
  {
    path: '/history',
    component: Layout,
    redirect: '/history/index',
    icon: 'el-icon-document',
    noDropdown: true,
    children: [{ path: 'index', name: '历史推送', component: _import('history/index'), meta: { role: ['admin'] }}]
  },
  {
    path: '/settings',
    component: Layout,
    redirect: '/settings/index',
    icon: 'el-icon-setting',
    noDropdown: true,
    children: [{ path: 'index', name: '基础设置', component: _import('settings/index'), meta: { role: ['admin'] }}]
  },
  { path: '*', redirect: '/404', hidden: true }
]
