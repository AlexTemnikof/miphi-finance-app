import { createStore } from 'vuex';
import {jwtDecode} from 'jwt-decode'

const store = createStore({
    state() {
        return {
            isAuthenticated: false,
            clientId: -1,
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
        setClientId(state, clientId) {
            state.clientId = clientId;
        }
    },
    actions: {
        initializeStore({ commit }) {
            const accessToken = localStorage.getItem('access_token');
            const clientId = localStorage.getItem('clientId')

            if (accessToken) {
                commit('setAuthenticated', true);
                commit('setAccessToken', accessToken);
            }
            if (clientId) {
                commit('setClientId', clientId);
            }
        },
        login({ commit }, accessToken) {
            localStorage.setItem('access_token', accessToken);
            const decodedToken = jwtDecode(accessToken);
            const clientId = decodedToken.sub;
            console.log('clientId ')
            console.log(clientId);
            localStorage.setItem('clientId', clientId);
            commit('setAccessToken', accessToken);
            commit('setAuthenticated', true);
            commit('setClientId', clientId);
        },
        logout({ commit }) {
            localStorage.removeItem('access_token');
            localStorage.removeItem('clientId');
            commit('setAccessToken', null);
            commit('setAuthenticated', false);
        },
    },
});

export default store;
