// src/routes.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import MainPage from './pages/MainPage';
import AuthPage from './pages/AuthPage';
import CityPage from './pages/CityPage';
import AdminListPage from './pages/AdminListPage';
import SpecialOpPage from './pages/SpecialOpPage';
import { Provider } from 'react-redux';
import store from './Store/store';

const AppRoutes = () => {
    //const user = useSelector(state => state.auth);
    return (
        <Provider store={store}>
            <Router>
                <Routes>
                    <Route path="/" element={<MainPage />} />
                    <Route path="/login" element={<AuthPage />} />
                    <Route path="/register" element={<AuthPage />} />
                    <Route path="/city-form" element={<CityPage />} />
                    <Route path="/admin-list" element={<AdminListPage />} />
                    <Route path="/special-operations" element={<SpecialOpPage />} />
                </Routes>
            </Router>
        </Provider>
    );
};

/*
        <Router>

            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/auth" element={<AuthPage />} />
                <Route path="/cities/create" element={<CityPage/>} />
                {user.auth ? (
                    <>
                        <Route path="/city/:id" element={<CityPage />} />
                        {user.isAdmin && <Route path="/admin" element={<AdminPage />} />}
                    </>
                    ) : (
                        <Route path="*" element={<AuthPage />} />
                    )*
</Routes>
</Router>
 */
export default AppRoutes;