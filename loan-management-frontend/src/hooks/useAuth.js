import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";

export const useAuth = () => {
    const { token, setToken, role, setRole, user, setUser, login, logout } = useContext(AuthContext);
    return { token, setToken, role, setRole, user, setUser, login, logout };
}