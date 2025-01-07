import apiClient from "../../apiClient.ts";

export const searchUsers = async (username: string) => {
    try{
        const response = await apiClient.get(`/users/search?username=${username}`);
        return response.data;
    } catch (error) {
        console.error("Error while accepting friendRequest", error);
    }
}