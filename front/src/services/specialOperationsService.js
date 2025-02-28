import { makeRequest, API_URL } from './service';

const SPEC_O_URL = API_URL + 'special-operations';

export const specialOperationsService = {
    calculateAverageMetersAboveSeaLevel: async () => {
        return await makeRequest(`${SPEC_O_URL}/average-meters-above-sea-level`, 'GET');
    },

    getCitiesByNameStartingWith: async (prefix) => {
        return await makeRequest(`${SPEC_O_URL}/cities-name-starts-with?prefix=${prefix}`, 'GET');
    },

    getCitiesByGovernorHeightLessThan: async (height) => {
        return await makeRequest(`${SPEC_O_URL}/cities-by-governor-height?height=${height}`, 'GET');
    },

    relocateAllPopulation: async (sourceCityId) => {
        return await makeRequest(`${SPEC_O_URL}/relocate-all-population?sourceCityId=${sourceCityId}`, 'GET');
    },

    relocateHalfOfCapitalPopulation: async () => {
        return await makeRequest(`${SPEC_O_URL}/relocate-half-capital-population`, 'GET');
    }
};