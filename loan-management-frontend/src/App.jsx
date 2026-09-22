import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'

import Login from './pages/auth/Login'
import AdminDashboard from './pages/admin/AdminDashboard'
import CustomerDashboard from './pages/customer/CustomerDashboard'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />

        <Route path="/login" element={<Login />} />

        <Route
          path="/admin/dashboard"
          element={<AdminDashboard />}
        />

        <Route
          path="/customer/dashboard"
          element={<CustomerDashboard />}
        />
      </Routes>
    </BrowserRouter>
  )
}

export default App