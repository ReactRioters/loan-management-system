const API_BASE_URL = "http://localhost:8080/api";

export async function createLoan(loanData) {
    const response = await fetch(`${API_BASE_URL}/loans`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(loanData)
    });

    if (!response.ok) {
        throw new Error("Failed to create loan");
    }

    return response.json();
}