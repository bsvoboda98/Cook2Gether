import { useState } from "react";
import { addRecipe } from "../../api/endpoints/recipes/addRecipe.ts";
import { postImage } from "../../api/endpoints/recipes/postImage.ts";
import NumericInput from "../NumericInput.tsx";
import DropdownInput from "../DropdownInput.tsx";
import ImageUploader from "../ImageUploader.tsx";

function CreateRecipeForm() {
    // State to store the form data
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        instructions: [],
        cookingTime: 0,
        preparationTime: 0,
        servings: 0,
        recipeIngredients: []
    });

    // State to store the new instruction
    const [newInstruction, setNewInstruction] = useState("");

    // State to store the new ingredient
    const [newIngredient, setNewIngredient] = useState("");

    // State to indicate if the form submission failed
    const [submitFailed, setSubmitFailed] = useState(false);

    // State to store the selected unit for ingredients
    const [selectedUnit, setSelectedUnit] = useState("g");

    // Options for the unit dropdown
    const options: string[] = [
        "g", "ml", "x", "el", "tl", "l", "kg"
    ];

    // State to store the amount of the new ingredient
    const [amount, setAmount] = useState("");

    // State to store the preview URL of the uploaded image
    const [imagePreviewUrl, setImagePreviewUrl] = useState<string | null>(null);

    // State to store the uploaded file
    const [file, setFile] = useState<File | null>(null);

    // State to store the preparation time
    const [preparationTime, setPreparationTime] = useState<string>("");

    // State to store the cooking time
    const [cookingTime, setCookingTime] = useState<string>("");

    // State to store the number of servings
    const [servings, setServings] = useState<string>("");

    // Function to handle input changes and update the form data
    const handleInputChange = (event) => {
        setFormData(prevData => ({
            ...prevData,
            [event.target.name]: event.target.value
        }));
    }

    // Function to handle instruction changes
    const handleInstructionChange = (value) => {
        setNewInstruction(value);
    }

    // Function to handle ingredient changes
    const handleIngredientChange = (value) => {
        setNewIngredient(value);
    }

    // Function to add a new instruction to the form data
    const addInstruction = () => {
        if (!newInstruction) return;
        setFormData(prevData => ({
            ...prevData,
            instructions: [...formData.instructions,
                { stepNumber: prevData.instructions.length + 1, instruction: newInstruction }
            ]
        }));
        setNewInstruction("");
    }

    // Function to add a new ingredient to the form data
    const addIngredient = () => {
        if (!newIngredient || !amount) return;
        setFormData(prevData => ({
            ...prevData,
            recipeIngredients: [...formData.recipeIngredients,
                { amount: Number(amount), unit: selectedUnit, name: newIngredient }
            ]
        }));
        setNewIngredient("");
    }

    // Function to handle form submission
    const handleSubmitClick = async () => {
        if (!formData.title || !formData.description || formData.instructions.length === 0 || !file) {
            setSubmitFailed(true);
        } else {
            setFormData(prevData => ({
                ...prevData,
                preparationTime: Number(preparationTime),
                cookingTime: Number(cookingTime),
                servings: Number(servings),
            }));

            const response = await addRecipe(formData);

            if (response.success) {
                alert("Rezept erfolgreich erstellt!");

                setFormData({
                    title: '',
                    description: '',
                    instructions: [],
                    cookingTime: 0,
                    preparationTime: 0,
                    servings: 0,
                    recipeIngredients: []
                });
                setNewIngredient("");
                setNewInstruction("");
                setSubmitFailed(false);

                const id = response.data.id;
                const imageResponse = await postImage({ file, id });
                if (!imageResponse.success) {
                    alert("Fehler beim Hochladen des Bildes.");
                }
            } else {
                alert("Fehler beim Erstellen des Rezepts.");
                setSubmitFailed(true);
            }
        }
    }

    // Render the component
    return (
        <form onSubmit={(e) => e.preventDefault()}>
            <h2>Rezept erstellen</h2>
            <div>
                <ImageUploader
                    file={file}
                    setFile={setFile}
                    imagePreviewUrl={imagePreviewUrl}
                    setImagePreviewUrl={setImagePreviewUrl}
                />
            </div>
            <div>
                <label>
                    Titel <br />
                    <input
                        type="text"
                        value={formData.title}
                        name="title"
                        onChange={(e) => handleInputChange(e)}
                    />
                </label>
            </div>
            <div>
                <label>
                    Beschreibung <br />
                    <textarea
                        value={formData.description}
                        name="description"
                        onChange={(e) => handleInputChange(e)}
                    />
                </label>
            </div>
            <div>
                <label>
                    Zubereitungsdauer (Minuten) <br />
                    <NumericInput value={preparationTime} setValue={setPreparationTime} />
                </label>
            </div>
            <div>
                <label>
                    Kochzeit (Minuten) <br />
                    <NumericInput value={cookingTime} setValue={setCookingTime} />
                </label>
            </div>
            <div>
                <label>
                    Portionen <br />
                    <NumericInput value={servings} setValue={setServings} />
                </label>
            </div>
            <div>
                <label>
                    Zutaten
                    <ul>
                        {formData.recipeIngredients.map((ingredient, index) => (
                            <li key={index}>
                                {ingredient.amount}{ingredient.unit} {ingredient.name}
                            </li>
                        ))}
                    </ul>

                    <div>
                        <NumericInput value={amount} setValue={setAmount} />
                        <DropdownInput
                            selectedValue={selectedUnit}
                            setSelectedValue={setSelectedUnit}
                            options={options}
                        />
                        <input
                            type="text"
                            value={newIngredient}
                            name="newIngredient"
                            onChange={(e) => handleIngredientChange(e.target.value)}
                        />
                    </div>

                    <button type="button" onClick={addIngredient}>add</button>
                </label>
            </div>
            <div>
                <label>
                    Instructions
                    <ol>
                        {formData.instructions.map((step) => (
                            <li key={step.stepNumber}>{step.instruction}</li>
                        ))}
                    </ol>
                    <textarea
                        value={newInstruction}
                        name="instruction"
                        onChange={(e) => handleInstructionChange(e.target.value)}
                    />
                    <br />
                    <button type="button" onClick={addInstruction}>add instruction</button>
                </label>
            </div>

            {submitFailed && (
                <div>
                    <p>Bitte gib mindestens den Titel, eine Beschreibung und die Zubereitungsschritte an.</p>
                </div>
            )}

            <button type="submit" onClick={handleSubmitClick}>Absenden</button>
        </form>
    );
}

export default CreateRecipeForm;