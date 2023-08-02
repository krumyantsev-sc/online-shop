import React from 'react';
import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Catalog from "./components/catalog/Catalog";
import AuthPage from "./components/auth/AuthPage";
import ProfilePage from "./components/profile/ProfilePage";
import BookPage from "./components/book/BookPage";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/catalog" element={<Catalog/>}/>
                <Route path="/login" element={<AuthPage/>}/>
                <Route path="/profile" element={<ProfilePage/>}/>
                <Route path="/catalog/:bookUuid" element={<BookPage/>}/>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
