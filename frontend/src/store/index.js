import { createStore } from 'vuex';

const store = createStore({
    state() {
        return {
            isAuthenticated: false,
            accessToken: null,
        };
    },
    mutations: {
        setAuthenticated(state, isAuthenticated) {
            state.isAuthenticated = isAuthenticated;
        },
        setAccessToken(state, accessToken) {
            state.accessToken = accessToken;
        },
    },
    actions: {
        initializeStore({ commit }) {
            const accessToken = localStorage.getItem('access_token');

            if (accessToken) {
                commit('setAuthenticated', true);
                commit('setAccessToken', accessToken);
            }
        },
        login({ commit }, accessToken) {
            localStorage.setItem('access_token', accessToken);
            commit('setAccessToken', accessToken);
            commit('setAuthenticated', true);
        },
        logout({ commit }) {
            localStorage.removeItem('access_token');
            commit('setAccessToken', null);
            commit('setAuthenticated', false);
        },
    },
});

export default store;
