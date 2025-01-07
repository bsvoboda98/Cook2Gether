import apiClient from "../../apiClient.ts";

export const rateRecipe = async (rating: number, id: number) => {
    try{
        const response = await apiClient.post("/recipe/rate", {
            recipeId: id,
            rating: rating
        })
        return response.data;
    } catch (error) {
        console.error("Error while rating the recipe");
    }
}