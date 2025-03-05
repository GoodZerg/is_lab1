import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { importHistoryService } from '../services/importHistoryService';

const ImportHistory = () => {
    const [history, setHistory] = useState([]);
    const userId = useSelector((state) => state.user.userId);
    const isAdmin = useSelector((state) => state.user.adminRole === 'ADMIN');

    useEffect(() => {
        const fetchHistory = async () => {
            try {
                const data = isAdmin
                    ? await importHistoryService.getAllHistory()
                    : await importHistoryService.getHistoryForUser(userId);
                setHistory(data);
            } catch (error) {
                console.error('Failed to fetch import history:', error);
            }
        };
        fetchHistory();
    }, [userId, isAdmin]);

    // Функция для скачивания файла
    const handleDownload = async (fileName) => {
        try {
            const response = await importHistoryService.downloadFile(fileName);
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', fileName); // Имя файла для скачивания
            document.body.appendChild(link);
            link.click();
            link.remove(); // Удаляем ссылку после скачивания
        } catch (error) {
            console.error('Failed to download file:', error);
        }
    };

    return (
        <div className="container mt-4">
            <h2>Import History</h2>
            <table className="table table-striped">
                <thead>
                <tr>
                    <th>User ID</th>
                    <th>Timestamp</th>
                    <th>Status</th>
                    <th>Added Objects</th>
                    <th>Action</th> {/* Новый столбец для кнопки */}
                </tr>
                </thead>
                <tbody>
                {history.map((record) => (
                    <tr key={record.id}> {/* Добавьте key для каждой строки */}
                        <td>{record.userId}</td>
                        <td>{record.timestamp}</td>
                        <td>{record.status}</td>
                        <td>{record.addedObjects || 'N/A'}</td>
                        <td>
                            {record.status === 'SUCCESS' && ( // Условие для отображения кнопки
                                <button
                                    className="btn btn-primary btn-sm"
                                    onClick={() => handleDownload(record.fileName)} // Обработчик скачивания
                                >
                                    Download
                                </button>
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default ImportHistory;