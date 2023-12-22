import axios from "axios";
import { createStore } from 'vuex';

const getters = {
    isAuthenticated: (state) => !!state.token,
};

const state = {
    token: localStorage.getItem('token') || null,
};

const mutations = {
    setToken(state, token) {
        state.token = token;
        localStorage.setItem('token', token);
    },
    clearToken(state) {
        state.token = null;
        localStorage.removeItem('token');
    },
};

const actions = {
    async login({ commit }, payload) {
        return axios.post('/auth/authenticate', payload)
            .then(response => {
                commit('setToken', response.data.token);
                // Perform other actions if needed
                return response;
            })
            .catch(error => {
                // Handle errors
                throw error;
            });
    },
    async register({ commit }, payload) {
        return axios.post('/auth/register', payload)
            .then(response => {
                commit('setToken', response.data.token);
                // Perform other actions if needed
                return response;
            })
            .catch(error => {
                // Handle errors
                throw error;
            });
    },
    logout({ commit }) {
        commit('clearToken');
    },
};
export default {
    state,
    mutations,
    actions,
    getters,
    // install(app) {
    //     app.use(createStore({
    //         state,
    //         mutations,
    //         actions,
    //         getters,
    //     }));
    //},
}
