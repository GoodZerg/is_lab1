import React, { useState } from 'react';
import { specialOperationsService } from '../services/specialOperationsService';

const SpecialOpList = () => {
    const [prefix, setPrefix] = useState('');
    const [height, setHeight] = useState('');
    const [sourceCityId, setSourceCityId] = useState('');

    const handleOperation = async (operation) => {
        try {
            let result;
            switch (operation) {
                case 'average':
                    result = await specialOperationsService.calculateAverageMetersAboveSeaLevel();
                    alert(`Average meters above sea level: ${result}`);
                    break;
                case 'prefix':
                    result = await specialOperationsService.getCitiesByNameStartingWith(prefix);
                    alert(`Cities: ${JSON.stringify(result)}`);
                    break;
                case 'height':
                    result = await specialOperationsService.getCitiesByGovernorHeightLessThan(Number(height));
                    alert(`Cities: ${JSON.stringify(result)}`);
                    break;
                case 'relocateAll':
                    result = await specialOperationsService.relocateAllPopulation(Number(sourceCityId));
                    alert(result);
                    break;
                case 'relocateHalf':
                    result = await specialOperationsService.relocateHalfOfCapitalPopulation();
                    alert(result);
                    break;
                default:
                    break;
            }
        } catch (error) {
            alert('Failed to perform operation: ' + error.message);
        }
    };

    return (
        <div className="container mt-4">
            <h2>Special Operations</h2>
            <div className="mb-3">
                <button className="btn btn-primary me-2" onClick={() => handleOperation('average')}>
                    Calculate Average Meters Above Sea Level
                </button>
            </div>
            <div className="mb-3">
                <input
                    type="text"
                    className="form-control mb-2"
                    placeholder="Prefix"
                    value={prefix}
                    onChange={(e) => setPrefix(e.target.value)}
                />
                <button className="btn btn-primary" onClick={() => handleOperation('prefix')}>
                    Get Cities by Name Starting With
                </button>
            </div>
            <div className="mb-3">
                <input
                    type="number"
                    className="form-control mb-2"
                    placeholder="Governor Height"
                    value={height}
                    onChange={(e) => setHeight(e.target.value)}
                />
                <button className="btn btn-primary" onClick={() => handleOperation('height')}>
                    Get Cities by Governor Height
                </button>
            </div>
            <div className="mb-3">
                <input
                    type="number"
                    className="form-control mb-2"
                    placeholder="Source City ID"
                    value={sourceCityId}
                    onChange={(e) => setSourceCityId(e.target.value)}
                />
                <button className="btn btn-primary" onClick={() => handleOperation('relocateAll')}>
                    Relocate All Population
                </button>
            </div>
            <div className="mb-3">
                <button className="btn btn-primary" onClick={() => handleOperation('relocateHalf')}>
                    Relocate Half of Capital Population
                </button>
            </div>
        </div>
    );
};

export default SpecialOpList;