import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getLoanDetails } from "../../services/loanService";

const LoanDetails = () => {
    const { id } = useParams();
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [loan, setLoan] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchLoanDetails = async () => {
            setLoading(true);
            setError(null);
            try {
                const loanData = await getLoanDetails(id);
                setLoan(loanData);
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
                        setError("Unable to load loan details. Please try again.");
                }
            } finally {
                setLoading(false);
            }
        };
        fetchLoanDetails();
    }, [id]);
    return (
        <div>
            <div>
                <button onClick={() => navigate(-1)}>Back</button>
                <h1>Loan Details</h1>
            </div>
            {loading && <p>Loading...</p>}
            {error && <p style={{ color: "red" }}>{error}</p>}
            {loan && (
                <div>
                    <p><strong>Loan ID:</strong> {loan.id}</p>
                    <p><strong>Loan Type:</strong> {loan.loanType}</p>
                    <p><strong>Amount:</strong> ₹{loan.amount.toFixed(2)}</p>
                    <p><strong>Tenure:</strong> {loan.tenure} years</p>
                    <p><strong>Status:</strong> {loan.status}</p>
                    <p><strong>Customer Name:</strong> {loan.customer.name}</p>
                    <p><strong>Customer Email:</strong> {loan.customer.email}</p>
                </div>
            )}
        </div>
    );
}

export default LoanDetails;