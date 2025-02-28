import React from 'react';
import NavigationComponent from '../components/NavigationComponent';
import AuthComponent from '../components/AuthComponent';

const AuthPage = () => {
    return (
        <div>
            <NavigationComponent />
            <div className="container mt-4">
                <h1>Auth Page</h1>
                <AuthComponent />
            </div>
        </div>
    );
};

export default AuthPage;