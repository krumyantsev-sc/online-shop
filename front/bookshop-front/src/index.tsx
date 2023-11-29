import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import {AuthProvider} from "./components/auth/context/AuthContextProvider";
import './i18n';
import {SocketProvider} from "./components/socket/SocketContext";

const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);
root.render(
    <AuthProvider>
        <SocketProvider>
            <App/>
        </SocketProvider>
    </AuthProvider>
);