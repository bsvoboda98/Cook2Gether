import { useNavigate } from "react-router-dom";
import styles from './RecipeMenu.module.css';

function RecipeMenu() {
    // Use the useNavigate hook to get the navigate function for navigating between routes
    const navigate = useNavigate();

    // Function to handle the Explore button click
    const handleExploreButton = () => {
        navigate(""); // Navigate to the root path
    }

    // Function to handle the Instant button click
    const handleInstantButton = () => {
        navigate(""); // Navigate to the root path
    }

    // Function to handle the Session button click
    const handleSessionButton = () => {
        navigate(""); // Navigate to the root path
    }

    // Function to handle the Test button click
    const handleTestButton = () => {
        navigate("/cook/test"); // Navigate to the test recipe page
    }

    // Function to handle the Search button click
    const handleSearchButton = () => {
        navigate(""); // Navigate to the root path
    }

    // Function to handle the Create button click
    const handleCreateButton = () => {
        navigate("/cook/create"); // Navigate to the create recipe page
    }

    return (
        <div className={styles["container"]}>
            <div>
                <button onClick={handleExploreButton}>Explore recipes</button>
            </div>
            <div>
                <button onClick={handleInstantButton}>Cook together</button>
            </div>
            <div>
                <button onClick={handleSessionButton}>Swipe together</button>
            </div>
            <div>
                <button onClick={handleTestButton}>Show recipe (test)</button>
            </div>
            <div>
                <button onClick={handleSearchButton}>Search</button>
            </div>
            <div>
                <button onClick={handleCreateButton}>Create</button>
            </div>
        </div>
    );
}

export default RecipeMenu;