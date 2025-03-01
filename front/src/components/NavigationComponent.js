import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { logOut } from '../Store/Slices/userSlice';

const NavigationComponent = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const isAuthenticated = useSelector((state) => state.user.auth);
    const isAdmin = useSelector((state) => state.user.adminRole);

    const handleLogout = () => {
        dispatch(logOut());
        navigate('/');
    };

    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <div className="container-fluid">
                <a className="navbar-brand" onClick={() => navigate('/')}>
                    City App
                </a>
                <div className="collapse navbar-collapse">
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                        <li className="nav-item">
                            <button className="btn btn-link nav-link" onClick={() => navigate('/')}>
                                Home
                            </button>
                        </li>
                        {isAuthenticated && isAdmin === "ADMIN" && (
                            <li className="nav-item">
                                <button className="btn btn-link nav-link" onClick={() => navigate('/admin-list')}>
                                    Admin Requests
                                </button>
                            </li>
                        )}
                        {isAuthenticated && (
                        <li className="nav-item">
                            <button className="btn btn-link nav-link" onClick={() => navigate('/special-operations')}>
                                Special Operations
                            </button>
                        </li>
                        )}
                    </ul>
                    <div className="d-flex">
                        {isAuthenticated ? (
                            <button className="btn btn-outline-danger" onClick={handleLogout}>
                                Logout
                            </button>
                        ) : (
                            <>
                                <button className="btn btn-outline-primary me-2" onClick={() => navigate('/login')}>
                                    Login
                                </button>
                                <button className="btn btn-outline-success" onClick={() => navigate('/register')}>
                                    Register
                                </button>
                            </>
                        )}
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default NavigationComponent;