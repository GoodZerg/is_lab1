import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { cityService } from '../services/cityService';
import { clear } from '../Store/Slices/chosenObjSlice';
import { useNavigate } from 'react-router-dom';

const CityForm = () => {
    const navigate = useNavigate();
    const [isEditable, setIsEditable] = useState(true);
    const chosenCity = useSelector((state) => state.chosenObj.city);
    const user = useSelector((state) => state.user);
    const [city, setCity] = useState({
        name: '',
        coordinates: { id: 0, x: 0, y: 0 },
        creationDate: Date.now(),
        establishmentDate: '',
        area: 0,
        population: 0,
        capital: false,
        metersAboveSeaLevel: 0,
        climate: '',
        government: '',
        standardOfLiving: '',
        governor: { id: 0, height: 0 },
        userId: user.userId,
    });

    const [coordinatesList, setCoordinatesList] = useState([]);
    const [governorsList, setGovernorsList] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [manualCoordinates, setManualCoordinates] = useState(false);
    const [manualGovernor, setManualGovernor] = useState(false);

    // Состояние для ошибок валидации
    const [validationErrors, setValidationErrors] = useState({});

    const climates = ['TROPICAL_SAVANNA', 'OCEANIC', 'MEDITERRANIAN', 'SUBARCTIC'];
    const governments = ['MATRIARCHY', 'NOOCRACY', 'TOTALITARIANISM', 'ETHNOCRACY'];
    const standardsOfLiving = ['VERY_HIGH', 'LOW', 'ULTRA_LOW'];

    // Загрузка данных
    useEffect(() => {
        const fetchData = async () => {
            try {
                const coordinates = await cityService.getAllCoordinates();
                const governors = await cityService.getAllGovernors();
                setCoordinatesList(coordinates);
                setGovernorsList(governors);
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, []);

    // Обновление состояния города
    useEffect(() => {
        if (Object.entries(chosenCity).length !== 0 && chosenCity.name !== '') {
            setCity({
                ...chosenCity,
                establishmentDate: chosenCity.establishmentDate.substring(0, 10),
            });
            if (chosenCity.userId === user.userId || user.adminRole === 'ADMIN') {
                setIsEditable(true);
            } else {
                setIsEditable(false);
            }
        }
    }, [chosenCity, user.userId, user.adminRole]);

    // Валидация поля
    const validateField = (name, value) => {
        let error = '';
        switch (name) {
            case 'name':
                if (!value || value.trim() === '') error = 'Name cannot be empty.';
                break;
            case 'coordinates.x':
            case 'coordinates.y':
                if (isNaN(value)) error = 'Coordinates must be numbers.';
                break;
            case 'area':
                if (!value || value <= 0) error = 'Area must be greater than 0.';
                break;
            case 'population':
                if (!value || value <= 0) error = 'Population must be greater than 0.';
                break;
            case 'governor.height':
                if (value <= 0) error = 'Governor height must be greater than 0.';
                break;
            case 'climate':
            case 'government':
            case 'standardOfLiving':
                if (!value) error = 'This field is required.';
                break;
            default:
                break;
        }
        return error;
    };

    // Обработка изменений в форме
    const handleChange = (e) => {
        if (!isEditable) return;

        const { name, value, type, checked } = e.target;
        const fieldValue = type === 'checkbox' ? checked : value;

        if (name.includes('.')) {
            const [parent, child] = name.split('.');
            setCity((prev) => ({
                ...prev,
                [parent]: { ...prev[parent], [child]: fieldValue },
            }));
        } else {
            setCity((prev) => ({
                ...prev,
                [name]: fieldValue,
            }));
        }

        // Очистка ошибки при изменении поля
        setValidationErrors((prev) => ({
            ...prev,
            [name]: '',
        }));
    };

    // Обработка отправки формы
    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!isEditable) return;

        // Проверка всех полей на валидность
        const errors = {};
        Object.keys(city).forEach((key) => {
            const error = validateField(key, city[key]);
            if (error) errors[key] = error;
        });

        // Проверка вложенных объектов (coordinates и governor)
        const coordinatesErrorX = validateField('coordinates.x', city.coordinates.x);
        const coordinatesErrorY = validateField('coordinates.y', city.coordinates.y);
        const governorErrorHeight = validateField('governor.height', city.governor.height);

        if (coordinatesErrorX) errors['coordinates.x'] = coordinatesErrorX;
        if (coordinatesErrorY) errors['coordinates.y'] = coordinatesErrorY;
        if (governorErrorHeight) errors['governor.height'] = governorErrorHeight;

        if (Object.keys(errors).length > 0) {
            setValidationErrors(errors);
            return;
        }

        try {
            const establishmentDate = city.establishmentDate
                ? city.establishmentDate + 'T00:00:00'
                : '2000-01-01T00:00:00';

            const cityData = {
                ...city,
                establishmentDate,
            };

            if (city.id) {
                await cityService.updateCity(cityData);
            } else {
                await cityService.createCity(cityData);
            }

            clear();
            navigate('/');
        } catch (error) {
            setError('Failed to save city: ' + error.message);
        }
    };

    // Обработка выбора координат
    const handleCoordinatesChange = (e) => {
        const selectedId = e.target.value;
        if (selectedId === 'manual') {
            setManualCoordinates(true);
            setCity((prev) => ({
                ...prev,
                coordinates: { id: 0, x: 0, y: 0 },
            }));
        } else {
            setManualCoordinates(false);
            const selectedCoordinates = coordinatesList.find((coord) => coord.id === Number(selectedId));
            setCity((prev) => ({
                ...prev,
                coordinates: selectedCoordinates || { id: 0, x: 0, y: 0 },
            }));
        }
    };

    // Обработка выбора губернатора
    const handleGovernorChange = (e) => {
        const selectedId = e.target.value;
        if (selectedId === 'manual') {
            setManualGovernor(true);
            setCity((prev) => ({
                ...prev,
                governor: { id: 0, height: 0 },
            }));
        } else {
            setManualGovernor(false);
            const selectedGovernor = governorsList.find((gov) => gov.id === Number(selectedId));
            setCity((prev) => ({
                ...prev,
                governor: selectedGovernor || { id: 0, height: 0 },
            }));
        }
    };

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <div className="container mt-4">
            <h2>City Form</h2>
            <form onSubmit={handleSubmit}>
                {/* Поле Name */}
                <div className="mb-3">
                    <label className="form-label">Name</label>
                    <input
                        type="text"
                        className={`form-control ${validationErrors.name ? 'is-invalid' : ''}`}
                        name="name"
                        value={city.name}
                        onChange={handleChange}
                        disabled={!isEditable}
                    />
                    {validationErrors.name && (
                        <div className="invalid-feedback">{validationErrors.name}</div>
                    )}
                </div>

                {/* Поле Coordinates */}
                <div className="mb-3">
                    <label className="form-label">Coordinates</label>
                    <select
                        className="form-select mb-2"
                        value={manualCoordinates ? 'manual' : city.coordinates.id}
                        onChange={handleCoordinatesChange}
                        disabled={!isEditable}
                    >
                        <option value={0}>Select Coordinates</option>
                        {coordinatesList.map((coord) => (
                            <option key={coord.id} value={coord.id}>
                                {coord.id}: ({coord.x}, {coord.y})
                            </option>
                        ))}
                        <option value="manual">Manual Input</option>
                    </select>
                    {manualCoordinates && (
                        <>
                            <input
                                type="number"
                                className={`form-control mb-2 ${validationErrors['coordinates.x'] ? 'is-invalid' : ''}`}
                                placeholder="X"
                                name="coordinates.x"
                                value={city.coordinates.x}
                                onChange={handleChange}
                                disabled={!isEditable}
                            />
                            {validationErrors['coordinates.x'] && (
                                <div className="invalid-feedback">{validationErrors['coordinates.x']}</div>
                            )}
                            <input
                                type="number"
                                className={`form-control ${validationErrors['coordinates.y'] ? 'is-invalid' : ''}`}
                                placeholder="Y"
                                name="coordinates.y"
                                value={city.coordinates.y}
                                onChange={handleChange}
                                disabled={!isEditable}
                            />
                            {validationErrors['coordinates.y'] && (
                                <div className="invalid-feedback">{validationErrors['coordinates.y']}</div>
                            )}
                        </>
                    )}
                </div>

                {/* Поле Area */}
                <div className="mb-3">
                    <label className="form-label">Area</label>
                    <input
                        type="number"
                        className={`form-control ${validationErrors.area ? 'is-invalid' : ''}`}
                        name="area"
                        value={city.area}
                        onChange={handleChange}
                        //min="0"
                        disabled={!isEditable}
                    />
                    {validationErrors.area && (
                        <div className="invalid-feedback">{validationErrors.area}</div>
                    )}
                </div>

                {/* Поле Population */}
                <div className="mb-3">
                    <label className="form-label">Population</label>
                    <input
                        type="number"
                        className={`form-control ${validationErrors.population ? 'is-invalid' : ''}`}
                        name="population"
                        value={city.population}
                        onChange={handleChange}
                        //min="0"
                        disabled={!isEditable}
                    />
                    {validationErrors.population && (
                        <div className="invalid-feedback">{validationErrors.population}</div>
                    )}
                </div>

                {/* Поле Establishment Date */}
                <div className="mb-3">
                    <label className="form-label">Establishment Date</label>
                    <input
                        type="date"
                        className="form-control"
                        name="establishmentDate"
                        value={city.establishmentDate}
                        onChange={handleChange}
                        disabled={!isEditable}
                    />
                </div>

                {/* Поле Capital */}
                <div className="mb-3 form-check">
                    <input
                        type="checkbox"
                        className="form-check-input"
                        name="capital"
                        checked={city.capital}
                        onChange={handleChange}
                        disabled={!isEditable}
                    />
                    <label className="form-check-label">Capital</label>
                </div>

                {/* Поле Meters Above Sea Level */}
                <div className="mb-3">
                    <label className="form-label">Meters Above Sea Level</label>
                    <input
                        type="number"
                        className="form-control"
                        name="metersAboveSeaLevel"
                        value={city.metersAboveSeaLevel}
                        onChange={handleChange}
                        disabled={!isEditable}
                    />
                </div>

                {/* Поле Climate */}
                <div className="mb-3">
                    <label className="form-label">Climate</label>
                    <select
                        className={`form-select ${validationErrors.climate ? 'is-invalid' : ''}`}
                        name="climate"
                        value={city.climate}
                        onChange={handleChange}
                        disabled={!isEditable}
                    >
                        <option value="">Select Climate</option>
                        {climates.map((climate, index) => (
                            <option key={index} value={climate}>
                                {climate}
                            </option>
                        ))}
                    </select>
                    {validationErrors.climate && (
                        <div className="invalid-feedback">{validationErrors.climate}</div>
                    )}
                </div>

                {/* Поле Government */}
                <div className="mb-3">
                    <label className="form-label">Government</label>
                    <select
                        className={`form-select ${validationErrors.government ? 'is-invalid' : ''}`}
                        name="government"
                        value={city.government}
                        onChange={handleChange}
                        disabled={!isEditable}
                    >
                        <option value="">Select Government</option>
                        {governments.map((government, index) => (
                            <option key={index} value={government}>
                                {government}
                            </option>
                        ))}
                    </select>
                    {validationErrors.government && (
                        <div className="invalid-feedback">{validationErrors.government}</div>
                    )}
                </div>

                {/* Поле Standard of Living */}
                <div className="mb-3">
                    <label className="form-label">Standard of Living</label>
                    <select
                        className={`form-select ${validationErrors.standardOfLiving ? 'is-invalid' : ''}`}
                        name="standardOfLiving"
                        value={city.standardOfLiving}
                        onChange={handleChange}
                        disabled={!isEditable}
                    >
                        <option value="">Select Standard of Living</option>
                        {standardsOfLiving.map((standard, index) => (
                            <option key={index} value={standard}>
                                {standard}
                            </option>
                        ))}
                    </select>
                    {validationErrors.standardOfLiving && (
                        <div className="invalid-feedback">{validationErrors.standardOfLiving}</div>
                    )}
                </div>

                {/* Поле Governor */}
                <div className="mb-3">
                    <label className="form-label">Governor</label>
                    <select
                        className="form-select mb-2"
                        value={manualGovernor ? 'manual' : city.governor.id}
                        onChange={handleGovernorChange}
                        disabled={!isEditable}
                    >
                        <option value={0}>Select Governor</option>
                        {governorsList.map((gov) => (
                            <option key={gov.id} value={gov.id}>
                                {gov.id}:({gov.height})
                            </option>
                        ))}
                        <option value="manual">Manual Input</option>
                    </select>
                    {manualGovernor && (
                        <input
                            type="number"
                            className={`form-control ${validationErrors['governor.height'] ? 'is-invalid' : ''}`}
                            placeholder="Height"
                            name="governor.height"
                            value={city.governor.height}
                            onChange={handleChange}
                            //min="0"
                            disabled={!isEditable}
                        />
                    )}
                    {validationErrors['governor.height'] && (
                        <div className="invalid-feedback">{validationErrors['governor.height']}</div>
                    )}
                </div>

                {error && <div className="text-danger mb-3">{error}</div>}
                <button
                    type="submit"
                    className="btn btn-success"
                    disabled={!isEditable}
                >
                    Submit
                </button>
            </form>
        </div>
    );
};

export default CityForm;