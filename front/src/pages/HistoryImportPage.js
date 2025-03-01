import React from 'react';
import NavigationComponent from '../components/NavigationComponent';
import ImportHistory from "../components/ImportHistory";

const HistoryImportPage = () => {
    return (
        <div>
            <NavigationComponent />
            <div className="container mt-4">
                <h1>Import History</h1>
                <ImportHistory />
            </div>
        </div>
    );
};

export default HistoryImportPage;