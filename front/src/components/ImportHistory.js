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
                </tr>
                </thead>
                <tbody>
                {history.map((record) => (
                    <tr>
                        <td>{record.userId}</td>
                        <td>{record.timestamp}</td>
                        <td>{record.status}</td>
                        <td>{record.addedObjects || 'N/A'}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default ImportHistory;