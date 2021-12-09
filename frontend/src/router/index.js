import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

import Home from '../views/Home.vue';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue'
import AddProject from '../views/AddProject.vue'
import AssignEmployeeToProject from '../views/AssignEmployeeToProject.vue'
import AddTask from '../views/AddTask.vue'

const routes = [
  {
    path:"/",
    name:"Home",
    component: Home
  },
  {
    path: "/login",
    name: "Login",
    component: Login
  },
  {
    path: "/register",
    name: "Register",
    component: Register
  },
  {
    path: "/addProject",
    name: "AddProject",
    component: AddProject
  },
  {
    path: "/assignEmployeeToProject",
    name: "AssignEmployeeToProject",
    component: AssignEmployeeToProject
  },
  {
    path: "/addTask",
    name: "AddTask",
    component: AddTask
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
