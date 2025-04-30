import {createRouter, createWebHistory} from 'vue-router'
import Login from '@/views/LoginPage.vue';
import Register from '@/views/RegisterPage.vue';
import MainPage from '@/views/MainPage.vue';
import store from '../store';
import EducationalResources from "@/views/EducationalResources.vue";
import EduOne from "@/views/EduOne.vue";
import EduTwo from "@/views/EduTwo.vue";
import EduThree from "@/views/EduThree.vue";


const routes = [
    { path: '/login', component: Login, meta: { guest: true } },
    { path: '/register', component: Register, meta: { guest: true } },
    { path: '/', component: MainPage, meta: { requiresAuth: true } },
    { path: "/educational-resources", component: EducationalResources, meta: { guest: true } },
    { path: "/educational-resources/1", component: EduOne, meta: { guest: true } },
    { path: "/educational-resources/2", component: EduTwo, meta: { guest: true } },
    { path: "/educational-resources/3", component: EduThree, meta: { guest: true } },
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

router.beforeEach((to, from, next) => {
    const isAuthenticated = store.state.isAuthenticated;

    if (to.matched.some(record => record.meta.requiresAuth) && !isAuthenticated) {
        next('/login');
    } else if (to.matched.some(record => record.meta.guest) && isAuthenticated) {
        next('/');
    } else {
        next();
    }
});

export default router