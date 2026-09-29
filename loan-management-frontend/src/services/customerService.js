import apiClient from "./apiClient";

export const getCustomerProfile = async (customerId) => {
    const response = await apiClient.get(`/customers/${customerId}`);
    return response.data;
}