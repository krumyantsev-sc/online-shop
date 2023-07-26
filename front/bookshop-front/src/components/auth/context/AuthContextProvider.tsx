import React, {createContext, useContext, useState} from 'react';

interface AuthContextInterface {
    isAuthenticated: boolean;
    login: (roles: string[]) => void;
    logout: () => void;
    roles: string[];
}

const AuthContext = createContext<AuthContextInterface>({
    isAuthenticated: false,
    login: (roles: string[]) => {
    },
    logout: () => {
    },
    roles: []
});

export const useAuth = () => {
    return useContext(AuthContext);
};

interface AuthProviderProps extends React.PropsWithChildren<{}> {
}

const TOKEN_KEY = "token";
const ROLES_KEY = "roles";

export const AuthProvider: React.FC<AuthProviderProps> = ({children}) => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(
        () => !!localStorage.getItem(TOKEN_KEY)
    );
    const rolesString = localStorage.getItem(ROLES_KEY);
    const [roles, setRoles] = useState<string[]>(rolesString ? rolesString.split(",") : []);


    const login = (roles: string[]) => {
        setIsAuthenticated(true);
        setRoles(roles);
    };

    const logout = () => {
        setIsAuthenticated(false);
        setRoles([]);
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(ROLES_KEY);
    };

    return (
        <AuthContext.Provider
            value={{isAuthenticated, login, logout, roles}}>
            {children}
        </AuthContext.Provider>
    );
};