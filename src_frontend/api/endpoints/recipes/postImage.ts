import apiClient from "../../apiClient.ts";


export const postImage = async ({file, id}: {file: File, id: number}): Promise<{success: boolean}> => {
    try{
        const formData = new FormData();
        formData.append('file', file);
        await apiClient.post(`/recipe/image/${id}`, formData);
        return{success: true};
    } catch (error) {
        console.error("Error while uploading image", error);
        return{success: false};
    }

}