import { createContext, useState } from "react";

export const AuthContext = createContext({

});

export const AuthProvider = ({ children }) => {
    const [token, setToken] = useState(localStorage.getItem("token") || null);
    const [role, setRole] = useState(localStorage.getItem("role") || null);
    const [user, setUser] = useState(JSON.parse(localStorage.getItem("user")) || null);

    const login = (authData) => {
        setRole(authData.role);
        setToken(authData.token);
        localStorage.setItem("token", authData.token);
        localStorage.setItem("role", authData.role);
        const userData = {
            id: authData.id,
            username: authData.username,
            email: authData.email,
            role: authData.role,
            customerId: authData.customerId,
            type: authData.type,
        };
        localStorage.setItem("user", JSON.stringify(userData));
        setUser(userData);
    }

    const logout = () => {
        setToken(null);
        setRole(null);
        setUser(null);
        localStorage.removeItem("token");
        localStorage.removeItem("role");
        localStorage.removeItem("user");
    }

    return (
        <AuthContext.Provider value={{ token, setToken, role, setRole, user, setUser, login, logout }}>
            {children}
        </AuthContext.Provider>
    )
}