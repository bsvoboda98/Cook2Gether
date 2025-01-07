import apiClient from "../../apiClient.ts";

export const getRecipeById = async (recipeId: number):Promise<{success: boolean, data?: any}> => {
    try{
        const response = await apiClient.get(`/recipe/${recipeId}`);
        return {success: true, data: response.data};
    } catch (error) {
        console.error("Error while fetching recipe", error);
        return {success: false};
    }
}