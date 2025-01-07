import apiClient from "../../apiClient.ts";

export const sendFriendRequest = async (receiverId: number) => {
    try{
        const response = await apiClient.post(`/users/sendFriendRequest/${receiverId}`);
        return response.data
    } catch (error) {
        console.error("Error while sending friendRequest", error);
    }
}