import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from './router'
import store from './store'
import axios from 'axios'
import VueAxios from 'vue-axios'

Vue.config.productionTip = false

Vue.use(VueAxios,axios)

axios.defaults.baseURL = process.env.VUE_APP_API_ENDPOINT

axios.interceptors.response.use(
  (response) => {
    return response;
  },
  (err) => {
    if(err.response.status === 401) {
      console.log("Refresh token.")
    }
  }
)

new Vue({
  vuetify,
  router,
  store,
  render: h => h(App)
}).$mount('#app')
