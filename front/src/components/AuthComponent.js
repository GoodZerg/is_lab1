import React, { useState } from 'react';
import { authService } from '../services/authService';
import { useNavigate } from 'react-router-dom';
import { logIn } from "../Store/Slices/userSlice";
import { useDispatch } from "react-redux";

const AuthComponent = () => {
    const [isLogin, setIsLogin] = useState(true);
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState('USER');
    const [error, setError] = useState(''); // Состояние для ошибки
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Проверка длины пароля
        if (password.length < 3) {
            setError('Password must be at least 3 characters long.');
            return;
        }

        try {
            if (isLogin) {
                const authResponse = await authService.login({ username, password });
                dispatch(logIn(authResponse));
                localStorage.setItem('token', authResponse.token);
                navigate('/');
            } else {
                await authService.register({ username, password, role });
                //alert('Registration successful! Please login.');
                setIsLogin(true);
            }
        } catch (error) {
            setError(error.message); // Отображение ошибки
        }
    };

    return (
        <div className="container mt-4">
            <h2>{isLogin ? 'Login' : 'Register'}</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label className="form-label">Username</label>
                    <input
                        type="text"
                        className="form-control"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required // Обязательное поле
                    />
                </div>
                <div className="mb-3">
                    <label className="form-label">Password</label>
                    <input
                        type="password"
                        className="form-control"
                        value={password}
                        onChange={(e) => {
                            setPassword(e.target.value);
                            setError(''); // Сброс ошибки при изменении пароля
                        }}
                        required // Обязательное поле
                    />
                    {error && <div className="text-danger">{error}</div>} {/* Отображение ошибки */}
                </div>
                {!isLogin && (
                    <div className="mb-3">
                        <label className="form-label">Role</label>
                        <select
                            className="form-select"
                            value={role}
                            onChange={(e) => setRole(e.target.value)}
                        >
                            <option value="USER">User</option>
                            <option value="ADMIN">Admin</option>
                        </select>
                    </div>
                )}
                <button
                    type="submit"
                    className="btn btn-primary"
                    disabled={password.length < 3} // Блокировка кнопки, если пароль слишком короткий
                >
                    {isLogin ? 'Login' : 'Register'}
                </button>
                <button
                    type="button"
                    className="btn btn-link"
                    onClick={() => setIsLogin(!isLogin)}
                >
                    {isLogin ? 'Need to register?' : 'Already have an account?'}
                </button>
            </form>
        </div>
    );
};

export default AuthComponent;