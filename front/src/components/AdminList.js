import React, { useState, useEffect } from 'react';
import { adminService } from '../services/adminService';

const AdminList = () => {
    const [requests, setRequests] = useState([]);

    useEffect(() => {
        const fetchRequests = async () => {
            try {
                const data = await adminService.getAllAdminRequests();
                setRequests(data);
            } catch (error) {
                console.error('Failed to fetch admin requests:', error);
            }
        };
        fetchRequests();
    }, []);

    const handleApprove = async (requestId) => {
        try {
            await adminService.approveAdminRequest(requestId);
            setRequests(requests.filter(req => req.id !== requestId));
            alert('Request approved successfully!');
        } catch (error) {
            alert('Failed to approve request: ' + error.message);
        }
    };

    return (
        <div className="container mt-4">
            <h2>Admin Requests</h2>
            <table className="table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>UserId</th>
                    <th>Status</th>
                    <th>Request_Date</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                {requests.map(request => (
                    <tr key={request.id}>
                        <td>{request.id}</td>
                        <td>{request.userId}</td>
                        <td>{request.status}</td>
                        <td>{request.requestDate}</td>
                        {request.status !== "APPROVED"?(
                        <td>
                            <button
                                className="btn btn-success"
                                onClick={() => handleApprove(request.id)}
                            >
                                Approve
                            </button>
                        </td>
                        ):(
                            <td>
                            </td>
                        )}
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default AdminList;