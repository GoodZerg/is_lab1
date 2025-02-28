import React from 'react';
import NavigationComponent from '../components/NavigationComponent';
import AdminList from '../components/AdminList';

const AdminListPage = () => {
    return (
        <div>
            <NavigationComponent />
            <div className="container mt-4">
                <h1>Admin Requests</h1>
                <AdminList />
            </div>
        </div>
    );
};

export default AdminListPage;