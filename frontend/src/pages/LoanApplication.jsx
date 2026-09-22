import { createLoan } from "../services/loanService";

function LoanApplication() {

    const handleSubmit = async (event) => {
        event.preventDefault();

        const loanData = {
            loanType: "PERSONAL",
            amount: 500000,
            tenure: 5
        };

        try {
            const response = await createLoan(loanData);

            console.log("Loan created:", response);
        } catch (error) {
            console.error("Loan creation failed:", error);
        }
    };
    return (
        <div>
            <h1>Loan Management System</h1>

            <h2>Apply for Loan</h2>

            <form onSubmit={handleSubmit}>
                <div>
                    <label>Loan Type</label>
                    <select>
                        <option value="">Select Loan Type</option>
                        <option value="personal">Personal Loan</option>
                        <option value="home">Home Loan</option>
                        <option value="car">Car Loan</option>
                    </select>
                </div>

                <div>
                    <label>Loan Amount</label>
                    <input
                        type="number"
                        placeholder="Enter loan amount"
                    />
                </div>

                <div>
                    <label>Tenure</label>
                    <input
                        type="number"
                        placeholder="Enter tenure in years"
                    />
                </div>

                <button type="submit">
                    Apply
                </button>
            </form>
        </div>
    );
}

export default LoanApplication;