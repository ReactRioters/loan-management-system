import { useState } from "react";
import { useAuth } from "../../hooks/useAuth";
import { login } from "../../services/authService";
import { useNavigate } from "react-router-dom";

function Login() {
  const { token, role, user, login: userLogin } = useAuth();
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  console.log("Auth state:", { token, role, user });
  const handleLogin = async (e) => {
    e.preventDefault();
    const response = await login(username, password);
    console.log("Login response:", response);
    userLogin(response);
    if (response.role === "ROLE_CUSTOMER") {
      navigate("/customer/dashboard");
    } else if (response.role === "ROLE_ADMIN") {
      navigate("/admin/dashboard");
    } else {
      console.error("Error: Unknown role");
    }
  }
  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-100">
      <div className="w-full max-w-md rounded-xl bg-white p-8 shadow-lg">
        <h1 className="text-2xl font-bold text-slate-800">
          Loan Management System
        </h1>
        <form className="mt-6" onSubmit={handleLogin}>
          <div>
            <label>Username</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
            />
          </div>
          <div className="mt-4">
            <label>Password</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
            />
          </div>
          <button type="submit" className="mt-4 w-full rounded-md bg-blue-500 py-2 text-white hover:bg-blue-600">
            Login
          </button>
        </form>


      </div>
    </div>
  )
}

export default Login