import { useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

const Header = () => {
    const { user, role, logout } = useAuth();
    const navigate = useNavigate();
    const handleLogout = () => {
        logout();
        navigate("/login");
    }
    return (
        <header>
            <h1>Loan Management System</h1>
            {user && (
                <nav>
                    <span>Welcome, {user.username}</span>
                    <p>Role: {role==="ROLE_ADMIN" ? "Admin": "Customer"}</p>
                    <button onClick={handleLogout}>Logout</button>
                </nav>
            )}
        </header>
    );
};

export default Header;