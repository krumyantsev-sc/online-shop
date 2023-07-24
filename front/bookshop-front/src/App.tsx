import React from 'react';
import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Catalog from "./components/catalog/Catalog";
import AuthPage from "./components/auth/AuthPage";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/catalog" element={<Catalog/>}/>
                <Route path="/login" element={<AuthPage/>}/>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
