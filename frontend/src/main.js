import { createApp } from 'vue';
import App from './App.vue';
import axios from 'axios';
import 'jquery';
import 'bootstrap/dist/css/bootstrap.css';
import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from './components/LoginPage.vue';
import RegisterPage from './components/RegisterPage.vue';
import DashboardPage from './components/DashboardPage.vue';
import EnvironmentList from "@/components/EnvironmentList.vue";
import EnvironmentCreate from "@/components/EnvironmentCreate.vue";
import EnvironmentEdit from "@/components/EnvironmentEdit.vue";

//axios.defaults.baseURL = import.meta.env.VITE_APP_API_URL
axios.defaults.baseURL = 'http://localhost:8080/api/v1';
// axios.interceptors.request.use(function (config) {
//     config.headers['X-Binarybox-Api-Key'] = import.meta.env.VUE_APP_API_KEY;
//     return config;
// });
console.log(import.meta.env.VITE_APP_API_KEY)


const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/', component: LoginPage },
        { path: '/register', component: RegisterPage },
        { path: '/dashboard', component: DashboardPage },
        {path: '/environments', component: EnvironmentList},
        { path: '/environments/new', component: EnvironmentCreate },
        { path: '/environments/edit/:id', component: EnvironmentEdit },
    ],
});

createApp(App).use(router).mount('#app');