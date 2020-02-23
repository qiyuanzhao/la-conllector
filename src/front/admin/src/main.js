import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-default/index.css'
import locale from 'element-ui/lib/locale/lang/en'
import App from './App'
import router from './router'
import store from './store'
import '@/icons' // icon
import '@/permission' // 权限
import axios from 'axios'

Vue.use(ElementUI, { locale })

Vue.config.productionTip = false

axios.defaults.baseURL = process.env.BASE_API;
axios.defaults.withCredentials= true;
// Add a response interceptor
axios.interceptors.response.use(function (response) {
  return response;
}, function (error) {
  console.log(typeof error);
  if (error.response.status === 401) {
    window.location.reload()
  } else {
    // Do something with response error
    return Promise.reject(error);
  }
});
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})
