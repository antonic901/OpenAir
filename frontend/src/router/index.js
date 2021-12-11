import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

import Home from '../views/Home.vue';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue'
import AddProject from '../views/AddProject.vue'
import AssignEmployeeToProject from '../views/AssignEmployeeToProject.vue'
import AddTask from '../views/AddTask.vue'
import RequestAbsence from '../views/RequestAbsence.vue'
import ApproveAbsence from '../views/ApproveAbsence.vue'
import LogTask from '../views/LogTask.vue'
import AddExpenseReport from '../views/AddExpenseReport.vue'
import ReviewExpenseReport from '../views/ReviewExpenseReport.vue'

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
  },
  {
    path: "/requestAbsence",
    name: "RequestAbsence",
    component: RequestAbsence
  },
  {
    path: "/approveAbsence",
    name: "ApproveAbsence",
    component: ApproveAbsence
  },
  {
    path: "/logTask",
    name: "LogTask",
    component: LogTask
  },
  {
    path: "/addExpenseReport",
    name: "AddExpenseReport",
    component: AddExpenseReport
  },
  {
    path: "/reviewExpenseReport",
    name: "ReviewExpenseReport",
    component: ReviewExpenseReport
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
