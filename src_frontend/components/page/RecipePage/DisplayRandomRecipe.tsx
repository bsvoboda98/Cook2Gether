import React, { useEffect, useState } from "react";
import { DetailedRecipe } from "../../../types/recipe.types.ts";
import { getRandomRecipe } from "../../../api/endpoints/recipes/getRandomRecipe.ts";
import RecipePage from "./RecipePage.tsx";

function DisplayRandomRecipe() {
    // State to store the fetched recipe
    const [recipe, setRecipe] = useState<DetailedRecipe | undefined>(undefined);

    // Use the useEffect hook to fetch a random recipe when the component mounts
    useEffect(() => {
        // Create an AbortController to handle canceling the request if the component unmounts
        const abortController = new AbortController();

        // Function to fetch a random recipe
        const fetchRandomRecipe = async () => {
            try {
                const response = await getRandomRecipe(abortController);
                if (response.success) {
                    // If the request is successful, set the recipe state
                    setRecipe(response.data);
                }
            } catch (error) {
                // Handle any errors that occur during the request
                console.error("Error fetching random recipe:", error);
            }
        };

        // Call the fetchRandomRecipe function
        fetchRandomRecipe();

        // Cleanup function to abort the request if the component unmounts
        return () => {
            console.log("clean up");
            abortController.abort();
        };
    }, []); // Empty dependency array ensures this effect runs only once on mount

    // Render the component
    return (
        <>
            {recipe ? (
                // If a recipe is fetched, render the RecipePage component with the recipe data
                <RecipePage recipe={recipe} />
            ) : (
                // If no recipe is fetched, display a loading message
                <p>Loading ...</p>
            )}
        </>
    );
}

export default DisplayRandomRecipe;