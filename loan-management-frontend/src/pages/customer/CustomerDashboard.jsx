import { useEffect, useState } from "react";
import { getLoanList } from "../../services/loanService";

function CustomerDashboard() {
  const [loans, setLoans] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  useEffect(() => {
    const fetchLoans = async () => {
      try {
        const loanData = await getLoanList();
        setLoans(loanData);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    fetchLoans();
  }, []);
  return (
    <div className="min-h-screen bg-slate-100 p-8">
      <h1 className="text-3xl font-bold text-slate-800">
        Customer Dashboard
      </h1>
      <div className="mt-6">
        <h2 className="text-xl font-semibold text-slate-700">
          Your Loans
        </h2>
        <ul className="mt-4 space-y-4">
          {loading && <li className="text-slate-600">Loading loans...</li>}
          {error && <li className="text-red-500">Error: {error}</li>}
          {/* loans.length === 0 */}
          {!loading && !error && loans.length === 0 && (
            <li className="text-slate-600">You have no loans.</li>
          )}
          {!loading && !error && loans.length > 0 && (
            loans.map((loan) => (
              <li key={loan.id} className="bg-white p-4 rounded shadow">
                <p><strong>Loan Type:</strong> {loan.loanType}</p>
                <p><strong>Amount:</strong> ₹{loan.amount}</p>
                <p><strong>Tenure:</strong> {loan.tenure} years</p>
                <p><strong>Status:</strong> {loan.status}</p>
              </li>
            )))}
        </ul>
      </div>
    </div>
  )
}

export default CustomerDashboard