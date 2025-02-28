import { createSlice } from '@reduxjs/toolkit';

const chosenObjSlice = createSlice({
    name: 'chosen',
    initialState: {
        city: {},
        column: '',
    },
    reducers: {
        clear(state){
            state.city = {};
        },
        setCity(state, action) {
            state.city = {...action.payload};
            console.log(state.city.capital);
            state.city.capital = state.city.capital === 't';
        },
        setColumn(state, action) {
            state.column = action.payload;
        },
    }
});

export const {clear, setCity, setColumn} = chosenObjSlice.actions;

export default chosenObjSlice.reducer;