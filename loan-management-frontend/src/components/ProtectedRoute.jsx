import { Navigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

const ProtectedRoute = ({ children, allowedRoles }) => {
  const { token, role } = useAuth();
console.log("ProtectedRoute token:", token);
console.log("ProtectedRoute role:", role);
console.log("Allowed roles:", allowedRoles);
  if (!token) {
    return <Navigate to="/login" />;
  }

  if (!allowedRoles.includes(role)) {
    return <Navigate to="/unauthorized" />;
  }

  return children;
};

export default ProtectedRoute;