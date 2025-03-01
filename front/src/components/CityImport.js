import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { cityService } from '../services/cityService';
import {useNavigate} from "react-router-dom";

const CityImport = () => {
    const navigate = useNavigate();
    const [file, setFile] = useState(null);
    const [message, setMessage] = useState('');
    const userId = useSelector((state) => state.user.userId); // Получаем userId из состояния

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!file) {
            setMessage('Please select a file.');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await cityService.importCities(formData, userId);
            setMessage(response.message);
            navigate("/");
        } catch (error) {
            setMessage('Error during import: ' + error.message);
        }
    };

    return (
        <div className="container mt-4">
            <h2>Import Cities</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label className="form-label">Upload JSON File</label>
                    <input
                        type="file"
                        className="form-control"
                        accept=".json"
                        onChange={handleFileChange}
                    />
                </div>
                <button type="submit" className="btn btn-primary">
                    Import
                </button>
            </form>
            {message && <div className="mt-3 alert alert-info">{message}</div>}
        </div>
    );
};

export default CityImport;