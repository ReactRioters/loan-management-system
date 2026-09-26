import apiClient from "./apiClient";

export const getLoanList = async () => {
    const response = await apiClient.get("/loans");
    return response.data;
};