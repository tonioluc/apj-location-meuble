import { createRouter, createWebHistory } from 'vue-router'
import ProformaList from '../views/ProformaList.vue'
import ProformaForm from '../views/ProformaForm.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/proformas' },
    { path: '/proformas', name: 'ProformaList', component: ProformaList },
    { path: '/proformas/new', name: 'ProformaForm', component: ProformaForm },
  ],
})

export default router
