import {API_URL, makeRequest} from './service';

const HIST_URL = API_URL +'import-history';

export const importHistoryService = {
    getHistoryForUser: async (userId) => {
        return await makeRequest(`${HIST_URL}/user?userId=${userId}`,"GET");
    },

    getAllHistory: async () => {
        return await makeRequest(`${HIST_URL}/admin`,"GET");
    },
};