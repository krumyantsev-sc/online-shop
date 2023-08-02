import React from 'react';
import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Catalog from "./components/catalog/Catalog";
import AuthPage from "./components/auth/AuthPage";
import ProfilePage from "./components/profile/ProfilePage";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/catalog" element={<Catalog/>}/>
                <Route path="/login" element={<AuthPage/>}/>
                <Route path="/profile" element={<ProfilePage/>}/>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
