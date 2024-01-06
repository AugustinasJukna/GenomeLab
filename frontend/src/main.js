import { createApp } from 'vue';
import App from './App.vue';
import axios from 'axios';
import 'jquery';
import 'bootstrap/dist/css/bootstrap.css';
import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from './components/LoginPage.vue';
import RegisterPage from './components/RegisterPage.vue';
import EnvironmentList from "@/components/EnvironmentList.vue";
import EnvironmentCreate from "@/components/EnvironmentCreate.vue";
import EnvironmentEdit from "@/components/EnvironmentEdit.vue";
import EnvironmentView from "@/components/EnvironmentView.vue";
import Main from "@/components/Main.vue";
import StateCreate from "@/components/StateCreate.vue";
import "./App.css";
import OrganismsList from "@/components/OrganismsList.vue";
import UserList from "@/components/UserList.vue";
import ProfilePage from "@/components/ProfilePage.vue";
import UserEdit from "@/components/UserEdit.vue";
import OrganismEdit from "@/components/OrganismEdit.vue";
import OrganismCreate from "@/components/OrganismCreate.vue";


axios.defaults.baseURL = import.meta.env.VITE_APP_API_URL;



const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/', component: Main },
        { path: '/login', component: LoginPage },
        { path: '/register', component: RegisterPage },
        {path: '/environments', component: EnvironmentList,
            beforeEnter: (to, from, next) => {
                const isAuthenticated = localStorage.getItem('token') !== null;
                if (isAuthenticated) {
                    next();
                } else {
                    next('/login');
                }
            },
        },

        { path: '/environments/new', component: EnvironmentCreate ,
            beforeEnter: (to, from, next) => {
                const isAuthenticated = localStorage.getItem('token') !== null;
                if (isAuthenticated) {
                    next();
                } else {
                    next('/login');
                }
            },},
        { path: '/environments/edit/:id', component: EnvironmentEdit ,
            beforeEnter: (to, from, next) => {
                const isAuthenticated = localStorage.getItem('token') !== null;
                if (isAuthenticated) {
                    next();
                } else {
                    next('/login');
                }
            },},
        { path: '/environments/view/:id', component: EnvironmentView ,
            beforeEnter: (to, from, next) => {
                const isAuthenticated = localStorage.getItem('token') !== null;
                if (isAuthenticated) {
                    next();
                } else {
                    next('/login');
                }
            },},
        { path: '/states/create/:id', component: StateCreate ,
            beforeEnter: (to, from, next) => {
                const isAuthenticated = localStorage.getItem('token') !== null;
                if (isAuthenticated) {
                    next();
                } else {
                    next('/login');
                }
            },},
        { path: '/environments/view/:envId/states/:stateId/organisms/edit/:organismId', component: OrganismEdit ,
            beforeEnter: (to, from, next) => {
                const isAuthenticated = localStorage.getItem('token') !== null;
                if (isAuthenticated) {
                    next();
                } else {
                    next('/login');
                }
            },},
        { path: '/environments/view/:envId/states/:stateId/organisms', component: OrganismsList ,
            beforeEnter: (to, from, next) => {
                const isAuthenticated = localStorage.getItem('token') !== null;
                if (isAuthenticated) {
                    next();
                } else {
                    next('/login');
                }
            },},
        { path: '/environments/view/:envId/states/:stateId/organisms/create', component: OrganismCreate ,
            beforeEnter: (to, from, next) => {
                const isAuthenticated = localStorage.getItem('token') !== null;
                if (isAuthenticated) {
                    next();
                } else {
                    next('/login');
                }
            },},
        { path: '/users', component: UserList,
            meta: { requiresAdmin: true }, // Add meta field for admin access
            beforeEnter: requireAdmin
        },
        { path: '/users/edit/:id', component: UserEdit,
            meta: { requiresAdmin: true }, // Add meta field for admin access
            beforeEnter: requireAdmin
        },
        {
            path: '/profile', component: ProfilePage,
            beforeEnter: (to, from, next) => {
                const isAuthenticated = localStorage.getItem('token') !== null;
                if (isAuthenticated) {
                    next();
                } else {
                    next('/login');
                }
            },
        },
    ],
});

function parseJwt(token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}

function requireAdmin(to, from, next) {
    const isAuthenticated = localStorage.getItem('token') !== null;
    if (isAuthenticated) {
        const decodedToken = parseJwt(localStorage.getItem('token'));
        const userRoles = decodedToken.role.split(',');

        if (to.matched.some(record => record.meta.requiresAdmin) && !userRoles.includes('ROLE_ADMIN')) {
            next('/');
        } else {
            next();
        }
    } else {
        next('/login');
    }
}

const app = createApp(App);
app.use(router);
app.mount('#app');