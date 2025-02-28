import React from 'react';
import { useNavigate } from 'react-router-dom';
import CityList from '../components/CityList';
import NavigationComponent from '../components/NavigationComponent';
import {useDispatch, useSelector} from "react-redux";
import {clear} from "../Store/Slices/chosenObjSlice";

const MainPage = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const handleCreateCity = () => {
        dispatch(clear());
        navigate('/city-form');
    };

    return (
        <div>
            <NavigationComponent />
            <div className="container mt-4">
                <h1>Main Page</h1>
                <button className="btn btn-primary mb-3" onClick={handleCreateCity}>
                    Create New City
                </button>
                <CityList />
            </div>
        </div>
    );
};

export default MainPage;