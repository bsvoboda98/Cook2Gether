import apiClient from "../../apiClient.ts";

export const getFriends = async () => {
    try {
        const response = await apiClient.get('/users/friends');
        return response.data;
    } catch (error) {
        console.error("Error while loading friends", error);
    }
}