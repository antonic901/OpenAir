import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
import createPersistedState from "vuex-persistedstate";

export default new Vuex.Store({
  strict: true,
  state: {
    jwt: null,
    userType: null
  },
  getters: {
    isUserLogged(state) {
      if(state.jwt === null) return false;
      else return true;
    },
    getJwt(state) {
      return state.jwt;
    },
    getUserType(state) {
      return state.userType;
    }
  },
  mutations: {
    updateJwt(state, jwt) {
      state.jwt = jwt;
    },
    updateUserType(state, userType) {
      state.userType = userType
    }
  },
  actions: {
    updateJwt(context, jwt) {
      context.commit('updateJwt', jwt);
    },
    updateUserType(context, userType) {
      context.commit('updateUserType', userType)
    }
  },
  plugins: [createPersistedState()],
})
