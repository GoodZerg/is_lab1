import { makeRequest, API_URL } from './service';

const CITY_URL = API_URL + 'cities';

export const cityService = {
    getAllCities: async () => {
        return await makeRequest(`${CITY_URL}/list`, 'GET');
    },

    getAllCoordinates: async () => {
        return await makeRequest(`${CITY_URL}/coordinates`, 'GET');
    },
    getAllGovernors: async () => {
        return await makeRequest(`${CITY_URL}/governors`, 'GET');
    },

    getCityById: async (id) => {
        return await makeRequest(`${CITY_URL}/get`, 'POST', { message: id.toString() });
    },

    createCity: async (cityDTO) => {
        return await makeRequest(`${CITY_URL}/create`, 'POST', cityDTO);
    },

    updateCity: async (cityDTO) => {
        return await makeRequest(`${CITY_URL}/update`, 'POST', cityDTO);
    },

    deleteCity: async (id) => {
        return await makeRequest(`${CITY_URL}/delete`, 'POST', { message: id.toString() });
    }
};