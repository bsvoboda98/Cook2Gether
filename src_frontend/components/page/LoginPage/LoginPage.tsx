import { Link } from "react-router-dom";
import { useContext, useEffect, useState } from "react";
import { verify } from "../../../api";
import { checkLogin } from "../../../api";
import styles from './LoginPage.module.css';
import { AuthContext } from "../../../utils/authContext.tsx";

function LoginPage() {
    // Extract the redirect parameter from the URL
    const params = new URLSearchParams(window.location.search);
    const redirectPathname = decodeURIComponent(params.get('redirect') || '');

    // Create an AbortController to handle canceling requests
    const abortController = new AbortController();

    // Retrieve the authentication state and functions from the AuthContext
    const { authState, login, logout } = useContext(AuthContext);

    // State to store the form data
    const [formData, setFormData] = useState({
        email: '',
        password: ''
    });

    // State to store any error messages
    const [error, setError] = useState<string | null>(null);

    // Function to redirect the user to the original page after login
    function redirectToOriginalPage() {
        if (redirectPathname) {
            window.location.href = redirectPathname;
        } else {
            window.location.href = '/home';
        }
    }

    // Use the useEffect hook to check the login status when the component mounts
    useEffect(() => {
        const check = async () => {
            await checkLogin({ authState, login, logout });
        }

        check();
    }, []); // Empty dependency array ensures this effect runs only once on mount

    // Function to handle input changes and update the form data
    const handleInputChange = (name, value) => {
        setFormData(prevData => ({
            ...prevData,
            [name]: value
        }));
    };

    // Function to handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault(); // Prevent the default form submission behavior

        // Call the verify function to authenticate the user
        const response = await verify({ authState, login, logout }, formData, abortController);

        if (!response.success) {
            // If the login fails, set the error message
            setError(response.error || 'An unknown error occurred');
        } else {
            // If the login is successful, redirect the user to the original page
            console.log("test");
            redirectToOriginalPage();
        }
    };

    return (
        <div className={styles['container']}>
            <div className={styles['loginWrapper']}>
                <form onSubmit={handleSubmit}>
                    <h1>Login</h1>
                    <div>
                        <input
                            className={styles['loginInput']}
                            type="text"
                            value={formData.email}
                            name="email"
                            onChange={(e) => handleInputChange(e.target.name, e.target.value)}
                            placeholder="Email"
                        />
                    </div>
                    <div>
                        <input
                            className={styles['loginInput']}
                            type="password"
                            value={formData.password}
                            name="password"
                            onChange={(e) => handleInputChange(e.target.name, e.target.value)}
                            placeholder="Password"
                        />
                    </div>
                    {error && <p className={styles['error']}>{error}</p>}
                    <button className={styles['loginButton']} type="submit">Login</button>
                    <p className={styles['registerParagraph']}>
                        <Link className={styles['registerLink']} to="/register">Jetzt registrieren</Link>
                    </p>
                </form>
            </div>
        </div>
    );
}

export default LoginPage;