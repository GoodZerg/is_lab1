import {API_URL, makeRequest} from './service';
import axios from "axios";

const HIST_URL = API_URL +'import-history';

export const importHistoryService = {
    getHistoryForUser: async (userId) => {
        return await makeRequest(`${HIST_URL}/user?userId=${userId}`,"GET");
    },

    getAllHistory: async () => {
        return await makeRequest(`${HIST_URL}/admin`,"GET");
    },

    downloadFile: async (fileName) => {
        try {
            return await axios.get(`${HIST_URL}/download/${fileName}`, {
                responseType: 'blob', // Указываем, что ожидаем бинарные данные
            });
        } catch (error) {
            throw new Error('Failed to download file');
        }
    },
};