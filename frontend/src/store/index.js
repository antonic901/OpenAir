import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)
import createPersistedState from "vuex-persistedstate";

export default new Vuex.Store({
  strict: true,
  state: {
    jwt: null,
    userType: null,
    userId: null
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
    },
    getUserId(state) {
      return state.userId;
    }
  },
  mutations: {
    updateJwt(state, jwt) {
      state.jwt = jwt;
    },
    updateUserType(state, userType) {
      state.userType = userType
    },
    updateUserId(state, userId) {
      state.userId = userId;
    }
  },
  actions: {
    updateJwt(context, jwt) {
      context.commit('updateJwt', jwt);
    },
    updateUserType(context, userType) {
      context.commit('updateUserType', userType)
    },
    updateUserId(context, userId) {
      context.commit('updateUserId', userId);
    }
  },
  plugins: [createPersistedState()],
})
