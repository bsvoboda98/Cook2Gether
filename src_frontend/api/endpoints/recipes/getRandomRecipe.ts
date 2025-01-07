import apiClient from "../../apiClient.ts";

export const getRandomRecipe = async (cancel: AbortController): Promise<{success: boolean, data?: any}> => {
    try{
        const response = await apiClient.get("/recipe/random", {signal: cancel.signal});
        return {success: true, data: response.data};
    } catch(error) {
        console.error("Error while fetching recipe", error);
        return {success: false};
    }
}