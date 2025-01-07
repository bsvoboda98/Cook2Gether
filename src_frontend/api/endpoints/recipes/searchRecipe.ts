import apiClient from "../../apiClient.ts";

export const searchRecipe = async (title: string) => {
    try{
        const response = await apiClient.get(`/recipe/search?title=${title}`);
        return response.data;
    } catch (error){
        console.error("Error while fetching recipe", error);
    }
}