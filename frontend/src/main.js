import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from './router'
import store from './store'
import axios from 'axios'
import VueAxios from 'vue-axios'

Vue.config.productionTip = false

Vue.use(VueAxios,axios)

axios.defaults.baseURL = process.env.VUE_APP_API_ENDPOINT;
// axios.defaults.headers.common['Authorization'] = 'Bearer ' + localStorage.jws;
// axios.defaults.headers.common['Content-Type'] = 'application/json';

// axios.interceptors.response.use(
//   (response) => {
//     return response;
//   },
//   (err) => {
//     console.log(err)
//     if(err.response.status === 401) {
//       alert("Token expired, pleae login again.")
//     }
//     else if(err.response.status === 409) {
//       alert(err.data)
//     }
//     else if(err.response.status === 404) {
//       alert(err.data)
//     }
//   }
// )

new Vue({
  vuetify,
  router,
  store,
  render: h => h(App)
}).$mount('#app')
