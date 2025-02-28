import { makeRequest, API_URL } from './service';

const AUTH_URL = API_URL + 'auth';

export const authService = {
    login: async (loginRequest) => {
        return await makeRequest(`${AUTH_URL}/login`, 'POST', loginRequest);
    },

    register: async (registrationDTO) => {
        return await makeRequest(`${AUTH_URL}/register`, 'POST', registrationDTO);
    }
};