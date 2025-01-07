import React, { useEffect, useState } from "react";
import StarRating from "../../StarRating.tsx";
import { rateRecipe } from "../../../api/endpoints/recipes/rateRecipe.ts";
import { getRecipeImage } from "../../../api/endpoints/recipes/getRecipeImage.ts";
import { DetailedRecipe } from "../../../types/recipe.types.ts";
import styles from "./RecipePage.module.css";
import CircleGraphic from "../../circleTime/CircleGraphic.tsx";

function RecipePage({ recipe }: { recipe: DetailedRecipe }) {
    // State to store the URL of the recipe image
    const [imageUrl, setImageUrl] = useState<string | null>(null);

    // Radius of the displayed circles
    const r: number = 32;

    // Use the useEffect hook to fetch the recipe image when the component mounts or the recipe changes
    useEffect(() => {
        if (!recipe) return;

        // Create an AbortController to handle canceling the request if the component unmounts
        const abortController = new AbortController();

        // Function to fetch the recipe image
        const fetchImage = async () => {
            if (!recipe.id) return;

            try {
                const response = await getRecipeImage(recipe.id, abortController);

                if (response.success) {
                    // Convert the response data to a Blob and create a URL for the image
                    const arrayBuffer = new Uint8Array(response.data);
                    const blob = new Blob([arrayBuffer], { type: 'image/jpeg' });
                    setImageUrl(URL.createObjectURL(blob));
                }
            } catch (error) {
                // Handle any errors that occur during the request
                console.error("Error fetching recipe image:", error);
            }
        };

        // Call the fetchImage function
        fetchImage();

        // Cleanup function to abort the request if the component unmounts
        return () => {
            console.log("clean up");
            abortController.abort();
        };
    }, [recipe]); // Dependency array includes 'recipe' to re-run the effect when 'recipe' changes

    // Function to handle rating the recipe
    function onRate(rating: number) {
        if (!recipe?.id) return;
        rateRecipe(rating, recipe.id);
    }

    // Render the component
    return (
        <div className={styles['container']}>
            {imageUrl ? (
                <div className={styles['imageContainer']}>
                    <img className={styles['image']} src={imageUrl} alt="Recipe Image" style={{ maxWidth: '100%', height: 'auto' }} />
                </div>
            ) : (
                <p>Loading image....</p>
            )}
            <h1 className={styles['title']}>{recipe?.title}</h1>

            <div className={styles['stats']}>
                <CircleGraphic radius={r} value={recipe.cookingTime} name="Kochzeit" />
                <CircleGraphic radius={r} value={recipe.preparationTime} name="Zubereitung" />
            </div>

            {recipe?.description && (
                <div className={styles['description']}>
                    <p>{recipe?.description}</p>
                </div>
            )}

            <div className={styles['ingredients']}>
                <ol className={styles['ingredientsList']}>
                    {recipe?.ingredients.map((ingredient) => (
                        <li className={styles['ingredientElement']} key={ingredient.name}>
                            <div className={styles['ingredientContainer']}>
                                <div className={styles['amountUnit']}>
                                    <p>{ingredient.amount + ingredient.unit}</p>
                                </div>
                                <div className={styles['ingredientName']}>
                                    <p>{ingredient.name}</p>
                                </div>
                            </div>
                        </li>
                    ))}
                </ol>
            </div>

            <div className={styles['preparation']}>
                <ol className={styles['instructionList']}>
                    {recipe?.instructions.map((instruction) => (
                        <li className={styles['instructionElement']} key={instruction.stepNumber}>
                            <p className={styles['instruction']}>{instruction.instruction}</p>
                        </li>
                    ))}
                </ol>
            </div>
        </div>
    );
}

export default RecipePage;