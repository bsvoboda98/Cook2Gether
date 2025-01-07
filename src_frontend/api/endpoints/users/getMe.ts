import apiClient from "../../apiClient.ts";

export const getMe = async () => {
    try{
        const response = await apiClient.get('/users/me');
        return response.data
    } catch (error) {

        console.error("Error while fetching user content", error);
    }
}