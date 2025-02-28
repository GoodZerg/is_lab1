import { createSlice } from '@reduxjs/toolkit';

const storageUsername = localStorage.getItem("username");
const storageToken = localStorage.getItem("token");
const storageAdminRole = localStorage.getItem("adminRole");
const storageUserId = localStorage.getItem("userId");



const userSlice = createSlice({
    name: 'user',
    initialState: {
        username: storageUsername || '',
        userId: storageUserId || 0,
        auth: Boolean(storageToken),
        token: storageToken || '',
        adminRole: storageAdminRole || '',
    },
    reducers: {
        logIn(state, action) {
            // const { username, token, adminRole } = action.payload;
            state.username = action.payload.username;
            state.userId = action.payload.userId;
            state.token = action.payload.token;
            console.log(action.payload.role);
            state.adminRole = action.payload.role;
            state.auth = true;
            localStorage.setItem("username", action.payload.username);
            localStorage.setItem("userId", action.payload.userId);
            localStorage.setItem("token", action.payload.token);
            localStorage.setItem("adminRole", action.payload.adminRole);
        },
        logOut(state, action) {
            localStorage.removeItem("username");
            localStorage.removeItem("userId");
            localStorage.removeItem("token");
            localStorage.removeItem("adminRole");
            state.username = '';
            state.userId = 0;
            state.token = '';
            state.adminRole = '';
            state.auth = false;
        }
    },
});

export const {logIn, logOut} = userSlice.actions;

export default userSlice.reducer;