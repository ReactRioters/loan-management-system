import { useEffect } from "react";
import { testProtectedApi } from "../../services/authService";

function CustomerDashboard() {
  useEffect(() => {
    testProtectedApi();
}, []);
  return (
    <div className="min-h-screen bg-slate-100 p-8">
      <h1 className="text-3xl font-bold text-slate-800">
        Customer Dashboard
      </h1>
    </div>
  )
}

export default CustomerDashboard