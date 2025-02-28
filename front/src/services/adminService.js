import { makeRequest, API_URL } from './service';

const ADM_URL = API_URL + 'admin';

export const adminService = {
    getAllAdminRequests: async () => {
        return await makeRequest(`${ADM_URL}/requests`, 'GET');
    },

    approveAdminRequest: async (requestId) => {
        return await makeRequest(`${ADM_URL}/approve`, 'POST', { message: requestId.toString() });
    }
};