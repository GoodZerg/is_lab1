import React from 'react';
import NavigationComponent from '../components/NavigationComponent';
import SpecialOpList from '../components/SpecialOpList';

const SpecialOpPage = () => {
    return (
        <div>
            <NavigationComponent />
            <div className="container mt-4">
                <h1>Special Operations</h1>
                <SpecialOpList />
            </div>
        </div>
    );
};

export default SpecialOpPage;