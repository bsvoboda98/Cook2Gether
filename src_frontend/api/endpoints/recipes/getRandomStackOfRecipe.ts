import apiClient from "../../apiClient.ts";

export const getRandomStackOfRecipe = async (count: number) => {
    try{
        const response = await apiClient.get(`recipes/random/${count}`);
        return response.data;
    } catch (error) {
        console.error("Error while fetching recipe", error);
    }
}