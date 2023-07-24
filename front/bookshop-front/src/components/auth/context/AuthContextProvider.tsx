import React, {createContext, useContext, useState} from 'react';

interface AuthContextInterface {
    isAuthenticated: boolean;
    login: () => void;
    logout: () => void;
    giveAdminAccess: () => void;
    makeGuest: () => void;
    isAdmin: boolean;
}

const AuthContext = createContext<AuthContextInterface>({
    isAuthenticated: false,
    login: () => {
    },
    logout: () => {
    },
    giveAdminAccess: () => {
    },
    makeGuest: () => {
    },
    isAdmin: false
});

export const useAuth = () => {
    return useContext(AuthContext);
};

interface AuthProviderProps extends React.PropsWithChildren<{}> {
}

export const AuthProvider: React.FC<AuthProviderProps> = ({children}) => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(
        () => !!localStorage.getItem("token")
    );
    const [isAdmin, setIsAdmin] = useState<boolean>(
        () => localStorage.getItem("role") === "ADMIN"
    );

    const login = () => {
        setIsAuthenticated(true);
    };

    const logout = () => {
        setIsAuthenticated(false);
        setIsAdmin(false);
        localStorage.removeItem("token");
        localStorage.removeItem("role");
    };

    const giveAdminAccess = () => {
        setIsAdmin(true);
        localStorage.setItem("role", "ADMIN");
    }

    const makeGuest = () => {
        setIsAdmin(false);
        localStorage.setItem("role", "guest");
    }

    return (
        <AuthContext.Provider
            value={{isAuthenticated, login, logout, giveAdminAccess, makeGuest, isAdmin}}>
            {children}
        </AuthContext.Provider>
    );
};