import { useCallback, useEffect, useState } from "react";
import { getLoanList } from "../../services/loanService";
import { useNavigate } from "react-router-dom";

function CustomerDashboard() {
  const [loans, setLoans] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const fetchLoans = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const loanData = await getLoanList();
      setLoans(loanData);
    } catch (err) {
      switch (err.response?.status) {
        case 401:
          setError("Unauthorized. Please log in again.");
          break;
        case 403:
          setError("Forbidden. You do not have permission to view this content.");
          break;
        case 404:
          setError("Not Found. The requested resource could not be found.");
          break;
        case 500:
          setError("Internal Server Error. Please try again later.");
          break;
        default:
          setError("Unable to load loans. Please try again.");
      }
    } finally {
      setLoading(false);
    }
  }, []);
  useEffect(() => {
    fetchLoans();
  }, [fetchLoans]);

  const viewLoanDetails = (id) => {
    navigate(`/customer/loans/${id}`);
  }
  return (
    <div className="min-h-screen bg-slate-100 p-8">
      <h1 className="text-3xl font-bold text-slate-800">
        Customer Dashboard
      </h1>
      <div className="mt-6">
        <h2 className="text-xl font-semibold text-slate-700">
          Your Loans
        </h2>
        {error && (
          <div className="mb-4 p-4 bg-red-100 text-red-700">
            <button onClick={fetchLoans} className="bg-red-500 text-white py-1 px-3 rounded hover:bg-red-600">
              Try Again
            </button>
          </div>
        )}
        {/* use table for list display also include view action for details loans */}
        <table className="min-w-full bg-white">
          <thead>
            <tr>
              <th className="py-2 px-4 border-b">Loan Type</th>
              <th className="py-2 px-4 border-b">Amount</th>
              <th className="py-2 px-4 border-b">Tenure</th>
              <th className="py-2 px-4 border-b">Status</th>
              <th className="py-2 px-4 border-b">Actions</th>
            </tr>
          </thead>
          <tbody>
            {loading && (
              <tr>
                <td colSpan="5" className="py-2 px-4 border-b">
                  Loading loans...
                </td>
              </tr>
            )}
            {error && (
              <tr>
                <td colSpan="5" className="py-2 px-4 border-b text-red-500">
                  Error: {error}
                </td>
              </tr>
            )}
            {/* loans.length === 0 */}
            {!loading && !error && loans.length === 0 && (
              <tr>
                <td colSpan="5" className="py-2 px-4 border-b">
                  You have no loans.
                </td>
              </tr>
            )}
            {!loading && !error && loans.length > 0 && (
              loans.map((loan) => (
                <tr key={loan.id}>
                  <td className="py-2 px-4 border-b">{loan.loanType}</td>
                  <td className="py-2 px-4 border-b">₹{loan.amount}</td>
                  <td className="py-2 px-4 border-b">{loan.tenure} years</td>
                  <td className="py-2 px-4 border-b">{loan.status}</td>
                  <td className="py-2 px-4 border-b">
                    <button onClick={() => viewLoanDetails(loan.id)} className="bg-blue-500 text-white py-1 px-3 rounded hover:bg-blue-600">
                      View Details
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  )
}

export default CustomerDashboard;