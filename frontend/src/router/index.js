import Vue from 'vue'
import VueRouter from 'vue-router'
import axios from 'axios'

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
import NotFound from '../views/NotFound.vue'

const routes = [
  {
    path:"/",
    name:"Home",
    component: Home,
    meta: { requiresAuth: true}
  },
  {
    path: "/login",
    name: "Login",
    component: Login
  },
  {
    path: "/register",
    // beforeEnter: (to, from, next) => {
    //   if(localStorage.jws) {
    //     axios.get("/auth/get-role", {headers: {'Authorization': `Bearer ` + localStorage.jws}})
    //       .then(r => {
    //         if(r.data != 'ROLE_ADMIN') {
    //           next({name: 'Home'})
    //         }
    //       })
    //   } else {
    //     next({name: 'Login'})
    //   }
      
    // },
    name: "Register",
    component: Register,
    meta: { requiresAdmin: true}
  },
  {
    path: "/addProject",
    name: "AddProject",
    component: AddProject,
    meta: { requiresAdmin: true}
  },
  {
    path: "/assignEmployeeToProject",
    name: "AssignEmployeeToProject",
    component: AssignEmployeeToProject,
    meta: { requiresAdmin: true}
  },
  {
    path: "/addTask",
    name: "AddTask",
    component: AddTask,
    meta: { requiresAdmin: true}
  },
  {
    path: "/requestAbsence",
    name: "RequestAbsence",
    component: RequestAbsence,
    meta: { requiresEmployee: true}
  },
  {
    path: "/approveAbsence",
    name: "ApproveAbsence",
    component: ApproveAbsence,
    meta: { requiresAdmin: true}
  },
  {
    path: "/logTask",
    name: "LogTask",
    component: LogTask,
    meta: { requiresEmployee: true}
  },
  {
    path: "/addExpenseReport",
    name: "AddExpenseReport",
    component: AddExpenseReport,
    meta: { requiresEmployee: true}
  },
  {
    path: "/reviewExpenseReport",
    name: "ReviewExpenseReport",
    component: ReviewExpenseReport,
    meta: { requiresAdmin: true}
  },
  {
    path: '/:catchAll(.*)',
    name: 'NotFound',
    component: NotFound
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  if(to.matched.some(record => record.meta.requiresAuth)) {
    if(!localStorage.jws) {
      next({path: '/login'})
    } else {
      next()
    }
  } else if(to.matched.some(record => record.meta.requiresEmployee)) {
    if(!localStorage.jws) {
      next({path: '/login'})
    } else {
      axios.get("/auth/role", {headers: {'Authorization': `Bearer ` + localStorage.jws}})
        .then(r => {
          if(r.data != 'ROLE_EMPLOYEE') {
            next({path: '/'})
          } else {
            next()
          }
        })
        .catch(() => {
          next({path: '/login'})
        })
    }
  } else if (to.matched.some(record => record.meta.requiresAdmin)) {
    if(!localStorage.jws) {
      next({path: '/login'})
    } else {
      axios.get("/auth/role", {headers: {'Authorization': `Bearer ` + localStorage.jws}})
        .then(r => {
          if(r.data != 'ROLE_ADMIN') {
            next({path: '/'})
          } else {
            next()
          }
        })
        .catch(() => {
          next({path: '/login'})
        })
    }
   } else {
    next()
  }
})

export default router
