import { configureStore } from '@reduxjs/toolkit'
import userReducer from './Slices/userSlice'
import collectionReducer from "./Slices/collectionSlice";
import chosenObjSlice from "./Slices/chosenObjSlice";

const store = configureStore({
    reducer: {
        collection: collectionReducer,
        user: userReducer,
        chosenObj: chosenObjSlice,
    },
})

export default store;