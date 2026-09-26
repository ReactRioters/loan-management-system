import apiClient from "./apiClient";

export const getLoanList = async () => {
    const response = await apiClient.get("/loans");
    return response.data;
};

export const getLoanDetails = async (loanId) => {
    const response = await apiClient.get(`/loans/${loanId}`);
    return response.data;
}