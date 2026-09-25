import apiClient from "./apiClient";

export const login = async (username, password) => {
    const response = await apiClient.post("/auth/login", {
        username,
        password
    });
    console.log("Login response:", response.data);
    return response.data;
}

export const testProtectedApi = async () => {
    const response = await apiClient.get("/loans");
    console.log("Protected API response:", response.data);
    return response.data;
}