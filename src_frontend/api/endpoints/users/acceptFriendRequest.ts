import apiClient from "../../apiClient.ts";

export const acceptFriendRequest = async (senderId: number) => {
    try{
        const response = await apiClient.post(`/users/acceptFriendRequest/${senderId}`);
        return response.data;
    } catch (error) {
        console.error("Error while accepting friendRequest", error);
    }
}