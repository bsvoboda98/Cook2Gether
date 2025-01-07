


export interface InitRecipe {
    title: string;
    description: string;
    instructions: [];
    cookingTime: number;
    preparationTime: number;
    servings: number;
    recipeIngredients: [];
}

export interface DetailedRecipe {
    id: number;
    title: string;
    description: string
    instructions: Instruction[];
    cookingTime: number;
    preparationTime: number;
    servings: number;
    ingredients: Ingredient[];
    rating: number;
}

export interface SmallRecipe {
    id: number;
    title: string;
    description: string;
    cookingTime: number;
    rating: number;
}

export interface Instruction {
    stepNumber: number;
    instruction: string;
}

export interface Ingredient {
    amount: number;
    unit: string;
    name: string;
}