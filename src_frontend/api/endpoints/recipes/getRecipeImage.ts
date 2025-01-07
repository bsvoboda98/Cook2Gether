import apiClient from "../../apiClient.ts";

export const getRecipeImage = async (id: number, abortController: AbortController): Promise<{success: boolean, data?: any}> => {

    try{
        const response = await apiClient.get(`/recipe/image/${id}`, {
            responseType: 'arraybuffer',
            signal: abortController.signal
        });
        return{success: true, data: response.data};
    } catch (error) {
        console.error("Error while fetching image of the recipe", error);
        return{success:false};
    }
}