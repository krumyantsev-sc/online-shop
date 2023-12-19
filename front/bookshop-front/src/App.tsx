import React from 'react';
import './App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Catalog from "./components/catalog/Catalog";
import AuthPage from "./components/auth/AuthPage";
import ProfilePage from "./components/profile/ProfilePage";
import BookPage from "./components/book/BookPage";
import OrderPage from "./components/order/OrderPage";
import OrderHistoryPage from "./components/order/OrderHistoryPage";
import MainPage from "./components/MainPage";
import AdminPage from "./components/admin/AdminPage";
import NotFoundPage from "./components/util/NotFoundPage";
import SupportPage from "./components/support/SupportPage";
import {SnackbarProvider} from 'notistack';
import AppSnackbar from "./components/util/AppSnackBar";
import StatsPage from "./components/admin/StatsPage";

function App() {
    return (
        <SnackbarProvider maxSnack={3}>
            <AppSnackbar />
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<MainPage/>}/>
                    <Route path="/catalog" element={<Catalog/>}/>
                    <Route path="/admin" element={<AdminPage/>}/>
                    <Route path="/login" element={<AuthPage/>}/>
                    <Route path="/profile" element={<ProfilePage/>}/>
                    <Route path="/catalog/:bookUuid" element={<BookPage/>}/>
                    <Route path="/order/:orderId" element={<OrderPage/>}/>
                    <Route path="/orders" element={<OrderHistoryPage/>}/>
                    <Route path="/support" element={<SupportPage/>}/>
                    <Route path="/admin/stats" element={<StatsPage/>}/>
                    <Route path="*" element={<NotFoundPage/>}/>
                </Routes>
            </BrowserRouter>
        </SnackbarProvider>
    );
}

export default App;