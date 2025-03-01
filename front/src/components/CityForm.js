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
        establishmentDate: '', // Добавлено поле establishmentDate
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

    const [coordinatesList, setCoordinatesList] = useState([]); // Список координат
    const [governorsList, setGovernorsList] = useState([]); // Список губернаторов
    const [loading, setLoading] = useState(true); // Состояние загрузки
    const [error, setError] = useState(null); // Состояние ошибки
    const [manualCoordinates, setManualCoordinates] = useState(false); // Ручной ввод координат
    const [manualGovernor, setManualGovernor] = useState(false); // Ручной ввод губернатора

    const climates = ['TROPICAL_SAVANNA', 'OCEANIC', 'MEDITERRANIAN', 'SUBARCTIC'];
    const governments = ['MATRIARCHY', 'NOOCRACY', 'TOTALITARIANISM', 'ETHNOCRACY'];
    const standardsOfLiving = ['VERY_HIGH', 'LOW', 'ULTRA_LOW'];

    // Загрузка списка координат и губернаторов
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

    // Обновление состояния города при изменении chosenCity
    useEffect(() => {
        if (Object.entries(chosenCity).length !== 0 && chosenCity.name !== '') {
            setCity(chosenCity);

            if (chosenCity.userId === user.userId || user.adminRole === 'ADMIN') {
                setIsEditable(true);
            } else {
                setIsEditable(false);
            }
        }
    }, [chosenCity, user.userId, user.adminRole]);

    // Обработка отправки формы
    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!isEditable) return;

        // Проверка значений
        if (city.population < 0 || city.area < 0 || city.governor.height < 0) {
            setError('Population, Area, and Governor height must be greater than or equal to 0.');
            return;
        }

        // Преобразование establishmentDate в формат LocalDateTime
        const establishmentDate = city.establishmentDate
            ? `${city.establishmentDate}T00:00:00` // Добавляем время
            : null;

        try {
            const cityData = {
                ...city,
                establishmentDate, // Используем преобразованное значение
            };

            let r;
            if (city.id) {
                r = await cityService.updateCity(cityData);
            } else {
                r = await cityService.createCity(cityData);
            }
            if (!r.ok)
                setError('Failed to save city: ');
            //alert('City saved successfully!');
            clear();
            navigate('/');
        } catch (error) {
            setError('Failed to save city: ' + error.message);
        }
    };

    // Обработка изменений в форме
    const handleChange = (e) => {
        if (!isEditable) return;

        const { name, value, type, checked } = e.target;
        if (name.includes('.')) {
            const [parent, child] = name.split('.');
            setCity((prev) => ({
                ...prev,
                [parent]: { ...prev[parent], [child]: type === 'checkbox' ? checked : value },
            }));
        } else {
            setCity((prev) => ({
                ...prev,
                [name]: type === 'checkbox' ? checked : value,
            }));
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
                <div className="mb-3">
                    <label className="form-label">Name</label>
                    <input
                        type="text"
                        className="form-control"
                        name="name"
                        value={city.name}
                        onChange={handleChange}
                        disabled={!isEditable}
                    />
                </div>
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
                                className="form-control mb-2"
                                placeholder="X"
                                value={city.coordinates.x}
                                onChange={(e) =>
                                    setCity((prev) => ({
                                        ...prev,
                                        coordinates: { ...prev.coordinates, x: e.target.value },
                                    }))
                                }
                                disabled={!isEditable}
                            />
                            <input
                                type="number"
                                className="form-control"
                                placeholder="Y"
                                value={city.coordinates.y}
                                onChange={(e) =>
                                    setCity((prev) => ({
                                        ...prev,
                                        coordinates: { ...prev.coordinates, y: e.target.value },
                                    }))
                                }
                                disabled={!isEditable}
                            />
                        </>
                    )}
                </div>
                <div className="mb-3">
                    <label className="form-label">Area</label>
                    <input
                        type="number"
                        className="form-control"
                        name="area"
                        value={city.area}
                        onChange={handleChange}
                        min="0" // Ограничение на минимальное значение
                        disabled={!isEditable}
                    />
                </div>
                <div className="mb-3">
                    <label className="form-label">Population</label>
                    <input
                        type="number"
                        className="form-control"
                        name="population"
                        value={city.population}
                        onChange={handleChange}
                        min="0" // Ограничение на минимальное значение
                        disabled={!isEditable}
                    />
                </div>
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
                <div className="mb-3">
                    <label className="form-label">Climate</label>
                    <select
                        className="form-select"
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
                </div>
                <div className="mb-3">
                    <label className="form-label">Government</label>
                    <select
                        className="form-select"
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
                </div>
                <div className="mb-3">
                    <label className="form-label">Standard of Living</label>
                    <select
                        className="form-select"
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
                </div>
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
                            className="form-control"
                            placeholder="Height"
                            value={city.governor.height}
                            onChange={(e) =>
                                setCity((prev) => ({
                                    ...prev,
                                    governor: { ...prev.governor, height: e.target.value },
                                }))
                            }
                            min="0" // Ограничение на минимальное значение
                            disabled={!isEditable}
                        />
                    )}
                </div>
                {error && <div className="text-danger mb-3">{error}</div>} {/* Отображение ошибки */}
                <button
                    type="submit"
                    className="btn btn-success"
                    disabled={
                        !isEditable ||
                        city.population < 0 ||
                        city.area < 0 ||
                        city.governor.height < 0
                    } // Блокировка кнопки, если значения не соответствуют требованиям
                >
                    Submit
                </button>
            </form>
        </div>
    );
};

export default CityForm;