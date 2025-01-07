import { useContext, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { signup } from "../../api";
import { AuthContext } from "../../utils/authContext.tsx";

function RegistrationForm() {
    // Use the useNavigate hook to get the navigate function for navigating between routes
    const navigate = useNavigate();

    // Use the useContext hook to access the AuthContext
    const authContext = useContext(AuthContext);

    // State to store any error messages
    const [error, setError] = useState<string | null>(null);

    // State to store the form data
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: ''
    });

    // Function to handle input changes and update the form data
    const handleInputChange = (name, value) => {
        setFormData(prevData => ({
            ...prevData,
            [name]: value
        }));
    }

    // Function to handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault(); // Prevent the default form submission behavior

        // Call the signup function to register the user
        const response = await signup(authContext, formData);

        if (response.success) {
            // If the registration is successful, navigate to the home page with the username in the state
            navigate('/home', { state: { username: formData.username } });
        } else {
            // If the registration fails, set the error message
            setError(response.error || 'Registration failed');
        }
    }

    // Render the component
    return (
        <>
            <h2>Erstelle ein neues Konto</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>
                        Username <br/>
                        <input
                            type="text"
                            value={formData.username}
                            name="username"
                            onChange={(e) => handleInputChange(e.target.name, e.target.value)}
                        />
                    </label>
                </div>
                <div>
                    <label>
                        Email <br/>
                        <input
                            type="text"
                            value={formData.email}
                            name="email"
                            onChange={(e) => handleInputChange(e.target.name, e.target.value)}
                        />
                    </label>
                </div>
                <div>
                    <label>
                        Password <br/>
                        <input
                            type="password" // Change input type to "password" for security
                            value={formData.password}
                            name="password"
                            onChange={(e) => handleInputChange(e.target.name, e.target.value)}
                        />
                    </label>
                </div>
                {error && <p style={{ color: 'red' }}>{error}</p>} {/* Display error message if there is one */}
                <button type="submit">Register</button>
            </form>
            <p>
                <Link to="/login">Du hast bereits ein Konto?</Link>
            </p>
        </>
    );
}

export default RegistrationForm;