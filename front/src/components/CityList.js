import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { cityService } from '../services/cityService';
import { useDispatch, useSelector } from 'react-redux';
import { setCity } from '../Store/Slices/chosenObjSlice';
import { setCities } from "../Store/Slices/collectionSlice";

const CityList = () => {
    const cities = useSelector(state => state.collection);
    const user = useSelector(state => state.user); // Получаем текущего пользователя
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [error, setError] = useState(null); // Состояние ошибки
    const [loading, setLoading] = useState(true); // Состояние загрузки

    // Состояние для пагинации
    const [currentPage, setCurrentPage] = useState(1); // Текущая страница
    const [itemsPerPage] = useState(4); // Количество элементов на странице

    // Состояние для фильтрации
    const [filter, setFilter] = useState(''); // Текущий фильтр

    // Состояние для сортировки
    const [sortConfig, setSortConfig] = useState({
        key: null, // Ключ сортировки (например, 'name', 'coordinates.id')
        direction: 'ascending', // Направление сортировки
    });

    useEffect(() => {
        const fetchCities = async () => {
            try {
                const data = await cityService.getAllCities();
                dispatch(setCities(data));
            } catch (error) {
                setError(error.message); // Обработка ошибки
            } finally {
                setLoading(false); // Загрузка завершена
            }
        };
        fetchCities();
    }, []);

    const handleRowClick = (city) => {
        dispatch(setCity(city));
        navigate('/city-form');
    };

    const handleDelete = async (cityId, userId) => {
        // Проверка прав на удаление
        if (userId !== user.userId && user.adminRole !== "ADMIN") {
            alert("You do not have permission to delete this city.");
            return;
        }

        try {
            await cityService.deleteCity(cityId); // Удаление города
            const updatedCities = cities.cities.filter(city => city.id !== cityId); // Обновление списка городов
            dispatch(setCities(updatedCities)); // Обновление состояния
            alert("City deleted successfully!");
        } catch (error) {
            alert("Failed to delete city: " + error.message);
        }
    };

    // Логика для фильтрации
    const filteredCities = cities.cities.filter(city => {
        const filterLower = filter.toLowerCase();
        return (
            (city.name?.toLowerCase() || '').includes(filterLower) || // Фильтрация по имени
            (city.coordinates.id?.toString() || '').includes(filterLower) || // Фильтрация по coordinates.id
            (city.area?.toString() || '').includes(filterLower) || // Фильтрация по area
            (city.population?.toString() || '').includes(filterLower) || // Фильтрация по population
            (city.establishmentDate?.toLowerCase() || '').includes(filterLower) || // Фильтрация по establishmentDate
            (city.capital ? 'yes' : 'no').includes(filterLower) || // Фильтрация по capital
            (city.metersAboveSeaLevel?.toString() || '').includes(filterLower) || // Фильтрация по metersAboveSeaLevel
            (city.climate?.toLowerCase() || '').includes(filterLower) || // Фильтрация по climate
            (city.government?.toLowerCase() || '').includes(filterLower) || // Фильтрация по government
            (city.standardOfLiving?.toLowerCase() || '').includes(filterLower) || // Фильтрация по standardOfLiving
            (city.governor.id?.toString() || '').includes(filterLower) || // Фильтрация по governor.id
            (city.userId?.toString() || '').includes(filterLower) // Фильтрация по userId
        );
    });

    // Логика для сортировки
    const sortedCities = [...filteredCities].sort((a, b) => {
        if (sortConfig.key) {
            let aValue, bValue;

            // Для вложенных объектов (coordinates и governor)
            if (sortConfig.key.includes('.')) {
                const [parent, child] = sortConfig.key.split('.');
                aValue = a[parent]?.[child] || ''; // Используем значение по умолчанию
                bValue = b[parent]?.[child] || ''; // Используем значение по умолчанию
            } else {
                aValue = a[sortConfig.key] || ''; // Используем значение по умолчанию
                bValue = b[sortConfig.key] || ''; // Используем значение по умолчанию
            }

            // Числовая сортировка для числовых полей
            if (typeof aValue === 'number' && typeof bValue === 'number') {
                return sortConfig.direction === 'ascending' ? aValue - bValue : bValue - aValue;
            }

            // Строковая сортировка для строковых полей
            if (typeof aValue === 'string' && typeof bValue === 'string') {
                return sortConfig.direction === 'ascending'
                    ? aValue.localeCompare(bValue)
                    : bValue.localeCompare(aValue);
            }
        }
        return 0;
    });

    // Логика для пагинации
    const indexOfLastItem = currentPage * itemsPerPage; // Индекс последнего элемента на странице
    const indexOfFirstItem = indexOfLastItem - itemsPerPage; // Индекс первого элемента на странице
    const currentItems = sortedCities.slice(indexOfFirstItem, indexOfLastItem); // Элементы для текущей страницы

    // Функция для изменения страницы
    const paginate = (pageNumber) => setCurrentPage(pageNumber);

    // Функция для изменения фильтра
    const handleFilterChange = (e) => {
        setFilter(e.target.value);
        setCurrentPage(1); // Сброс страницы при изменении фильтра
    };

    // Функция для сортировки
    const requestSort = (key) => {
        let direction = 'ascending';
        if (sortConfig.key === key && sortConfig.direction === 'ascending') {
            direction = 'descending';
        }
        setSortConfig({ key, direction });
    };

    if (loading) {
        return <div>Loading...</div>; // Отображение загрузки
    }

    if (error) {
        return <div>Error: {error}</div>; // Отображение ошибки
    }

    return (
        <div className="container mt-4">
            <h2>City List</h2>

            {/* Поле для фильтрации */}
            <div className="mb-3">
                <input
                    type="text"
                    className="form-control"
                    placeholder="Filter by any column..."
                    value={filter}
                    onChange={handleFilterChange}
                />
            </div>

            <table className="table table-hover">
                <thead>
                <tr>
                    <th onClick={() => requestSort('name')}>Name {sortConfig.key === 'name' ? (sortConfig.direction === 'ascending' ? '▲' : '▼') : ''}</th>
                    <th onClick={() => requestSort('coordinates.id')}>Coordinates {sortConfig.key === 'coordinates.id' ? (sortConfig.direction === 'ascending' ? '▲' : '▼') : ''}</th>
                    <th onClick={() => requestSort('creationDate')}>Creation Date {sortConfig.key === 'creationDate' ? (sortConfig.direction === 'ascending' ? '▲' : '▼') : ''}</th>
                    <th onClick={() => requestSort('area')}>Area {sortConfig.key === 'area' ? (sortConfig.direction === 'ascending' ? '▲' : '▼') : ''}</th>
                    <th onClick={() => requestSort('population')}>Population {sortConfig.key === 'population' ? (sortConfig.direction === 'ascending' ? '▲' : '▼') : ''}</th>
                    <th onClick={() => requestSort('establishmentDate')}>Establishment Date {sortConfig.key === 'establishmentDate' ? (sortConfig.direction === 'ascending' ? '▲' : '▼') : ''}</th>
                    <th onClick={() => requestSort('capital')}>Capital {sortConfig.key === 'capital' ? (sortConfig.direction === 'ascending' ? '▲' : '▼') : ''}</th>
                    <th onClick={() => requestSort('metersAboveSeaLevel')}>Meters Above Sea Level {sortConfig.key === 'metersAboveSeaLevel' ? (sortConfig.direction === 'ascending' ? '▲' : '▼') : ''}</th>
                    <th onClick={() => requestSort('climate')}>Climate {sortConfig.key === 'climate' ? (sortConfig.direction === 'ascending' ? '▲' : '▼') : ''}</th>
                    <th onClick={() => requestSort('government')}>Government {sortConfig.key === 'government' ? (sortConfig.direction === 'ascending' ? '▲' : '▼') : ''}</th>
                    <th onClick={() => requestSort('standardOfLiving')}>Standard of Living {sortConfig.key === 'standardOfLiving' ? (sortConfig.direction === 'ascending' ? '▲' : '▼') : ''}</th>
                    <th onClick={() => requestSort('governor.id')}>Governor {sortConfig.key === 'governor.id' ? (sortConfig.direction === 'ascending' ? '▲' : '▼') : ''}</th>
                    <th onClick={() => requestSort('userId')}>User ID {sortConfig.key === 'userId' ? (sortConfig.direction === 'ascending' ? '▲' : '▼') : ''}</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                {currentItems.map(city => (
                    <tr key={city.id} style={{cursor: 'pointer'}}>
                        <td onClick={() => handleRowClick(city)}>{city.name}</td>
                        <td onClick={() => handleRowClick(city)}>{city.coordinates.id}:({city.coordinates.x}, {city.coordinates.y})</td>
                        <td onClick={() => handleRowClick(city)}>{city.creationDate}</td>
                        <td onClick={() => handleRowClick(city)}>{city.area}</td>
                        <td onClick={() => handleRowClick(city)}>{city.population}</td>
                        <td onClick={() => handleRowClick(city)}>{city.establishmentDate}</td>
                        <td onClick={() => handleRowClick(city)}>{city.capital ? 'Yes' : 'No'}</td>
                        <td onClick={() => handleRowClick(city)}>{city.metersAboveSeaLevel}</td>
                        <td onClick={() => handleRowClick(city)}>{city.climate}</td>
                        <td onClick={() => handleRowClick(city)}>{city.government}</td>
                        <td onClick={() => handleRowClick(city)}>{city.standardOfLiving}</td>
                        <td onClick={() => handleRowClick(city)}>{city.governor.id}:({city.governor?.height || 'N/A'})</td>
                        <td onClick={() => handleRowClick(city)}>{city.userId}</td>
                        <td>
                            <button
                                className="btn btn-danger btn-sm"
                                onClick={(e) => {
                                    e.stopPropagation(); // Останавливаем всплытие события
                                    handleDelete(city.id, city.userId);
                                }}
                                disabled={city.userId !== user.userId && user.adminRole !== "ADMIN"} // Блокировка кнопки
                            >
                                Delete
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            {/* Пагинация */}
            <nav>
                <ul className="pagination">
                    {Array.from({length: Math.ceil(sortedCities.length / itemsPerPage)}, (_, i) => (
                        <li key={i + 1} className={`page-item ${currentPage === i + 1 ? 'active' : ''}`}>
                            <button onClick={() => paginate(i + 1)} className="page-link">
                                {i + 1}
                            </button>
                        </li>
                    ))}
                </ul>
            </nav>
        </div>
    );
};

export default CityList;