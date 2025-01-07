import apiClient from "../../apiClient.ts";

export const getFriendRequests = async () => {
    try{
        const response = await apiClient.get('/users/friendRequests');
        return response.data;
    } catch (error) {
        console.error("Error while fetching incoming getFriendRequests", error);
    }
}