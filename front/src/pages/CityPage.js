import React from 'react';
import NavigationComponent from '../components/NavigationComponent';
import CityForm from '../components/CityForm';

const CityPage = () => {
    return (
        <div>
            <NavigationComponent />
            <div className="container mt-4">
                <h1>City Page</h1>
                <CityForm />
            </div>
        </div>
    );
};

export default CityPage;