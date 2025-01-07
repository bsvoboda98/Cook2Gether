import apiClient from "../../apiClient.ts";
import {InitRecipe} from "../../../types/recipe.types.ts";

export const addRecipe = async (formData: InitRecipe): Promise<{success: boolean, data?: any}> => {
    try{
        const response = await apiClient.post("/recipe/add", formData);
        return{success: true,data: response.data}
    } catch (error){
        console.error("Error while creating recipe", error);
        return{success: false};
    }
}