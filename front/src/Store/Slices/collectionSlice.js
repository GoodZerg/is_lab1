import { createSlice } from '@reduxjs/toolkit';

const sampleCities = [
    {id: 1,
    area: 1,
    capital: 't',
    climate: 'TROPICAL_SAVANNA',
        creationDate: '2025-02-26 23:42:40',
        establishmentDate: '2025-02-26 23:42:43',
    government: 'MATRIARCHY',
        metersAboveSeaLevel: 1,
    name: 'asd',
    population: 1,
        standardOfLiving: 'VERY_HIGH',
    coordinates: {id:2, x:2, y:2},
    governor: {id:1, height:1},
        userId: 77
    },
    {
    id: 2,
    area: 1,
    capital: 't',
    climate: 'TROPICAL_SAVANNA',
        creationDate: '2025-02-26 23:42:40',
        establishmentDate: '2025-02-26 23:42:43',
    government: 'MATRIARCHY',
        metersAboveSeaLevel: 1,
    name: 'asdd',
    population: 1,
        standardOfLiving: 'VERY_HIGH',
        coordinates: {id:2, x:2, y:2},
        governor: {id:1, height:1},
        userId: 77
    }
];

const collectionSlice = createSlice({
    name: 'collection',
    initialState: {
        cities: sampleCities
    },
    reducers: {
        addCity(state, action) {
            state.cities.unshift(action.payload);
        },
        setCities(state, action) {
            state.cities = action.payload;
        },
        clearCities(state) {
            state.cities = [];
        },

    }
});

export const { addCity, setCities, clearCities} = collectionSlice.actions;

export default collectionSlice.reducer;