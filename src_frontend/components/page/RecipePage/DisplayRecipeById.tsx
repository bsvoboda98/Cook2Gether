import React, { useEffect, useState } from 'react';
import { useParams } from "react-router-dom";
import { DetailedRecipe } from "../../../types/recipe.types.ts";
import { getRecipeById } from "../../../api/endpoints/recipes/getRecipeById.ts";
import RecipePage from "./RecipePage.tsx";

function DisplayRecipeById() {
    // Use the useParams hook to get the 'id' parameter from the URL
    const { id } = useParams();

    // State to store the fetched recipe
    const [recipe, setRecipe] = useState<DetailedRecipe | undefined>(undefined);

    // Use the useEffect hook to fetch the recipe by ID when the component mounts or the 'id' parameter changes
    useEffect(() => {
        // Function to fetch the recipe by ID
        const fetchRecipeById = async () => {
            if (!id) return; // If there is no ID, return early

            try {
                const response = await getRecipeById(parseInt(id));
                if (response.success) {
                    // If the request is successful, set the recipe state
                    setRecipe(response.data);
                }
            } catch (error) {
                // Handle any errors that occur during the request
                console.error("Error fetching recipe by ID:", error);
            }
        };

        // Call the fetchRecipeById function
        fetchRecipeById();
    }, [id]); // Dependency array includes 'id' to re-run the effect when 'id' changes

    // Render the component
    return (
        <>
            {recipe ? (
                <div>
                    {/* If a recipe is fetched, render the RecipePage component with the recipe data */}
                    <RecipePage recipe={recipe} />
                </div>
            ) : (
                <div>
                    {/* If no recipe is fetched, display an error message */}
                    <p>Rezept konnte nicht gefunden werden!</p>
                </div>
            )}
        </>
    );
}

export default DisplayRecipeById;