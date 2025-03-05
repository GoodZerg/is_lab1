import React, { useState } from 'react';
import { specialOperationsService } from '../services/specialOperationsService';

const SpecialOpList = () => {
    const [prefix, setPrefix] = useState('');
    const [height, setHeight] = useState('');
    const [sourceCityId, setSourceCityId] = useState('');
    const [result, setResult] = useState(null); // Состояние для хранения результата операции
    const [error, setError] = useState(null); // Состояние для хранения ошибки

    const handleOperation = async (operation) => {
        try {
            let response;
            switch (operation) {
                case 'average':
                    response = await specialOperationsService.calculateAverageMetersAboveSeaLevel();
                    setResult(`Average meters above sea level: ${response.average}`);
                    break;
                case 'prefix':
                    response = await specialOperationsService.getCitiesByNameStartingWith(prefix);
                    setResult(`Cities: ${JSON.stringify(response, null, 2)}`);
                    break;
                case 'height':
                    response = await specialOperationsService.getCitiesByGovernorHeightLessThan(Number(height));
                    setResult(`Cities: ${JSON.stringify(response, null, 2)}`);
                    break;
                case 'relocateAll':
                    response = await specialOperationsService.relocateAllPopulation(Number(sourceCityId));
                    setResult(response.message);
                    break;
                case 'relocateHalf':
                    response = await specialOperationsService.relocateHalfOfCapitalPopulation();
                    setResult(response.message);
                    break;
                default:
                    break;
            }
            setError(null); // Сброс ошибки при успешном выполнении
        } catch (error) {
            setError('Failed to perform operation: ' + error.message);
            setResult(null); // Сброс результата при ошибке
        }
    };

    return (
        <div className="container mt-4">
            <h2>Special Operations</h2>

            {/* Отображение ошибки */}
            {error && (
                <div className="alert alert-danger" role="alert">
                    {error}
                </div>
            )}

            {/* Отображение результата */}
            {result && (
                <div className="alert alert-success" role="alert">
                    <pre>{result}</pre>
                </div>
            )}

            {/* Кнопки и поля ввода */}
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