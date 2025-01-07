import { Link } from "react-router-dom";
import styles from './TopBar.module.css';
import { AuthContext } from "../../../utils/authContext.tsx";
import { useContext, useEffect } from "react";
import { apiLogout, checkLogin } from "../../../api";

function TopBar() {
    // Retrieve the authentication state and functions from the AuthContext
    const { authState, login, logout } = useContext(AuthContext);

    // Use the useEffect hook to check the login status when the component mounts
    useEffect(() => {
        const check = async () => {
            // Call the checkLogin function to verify the user's authentication status
            await checkLogin({ authState, login, logout });
        }

        // Execute the check function
        check();
    }, []); // Empty dependency array ensures this effect runs only once on mount

    // Function to handle the login button click
    const handleLoginClick = () => {
        // Redirect the user to the login page with the current path and hash as query parameters
        window.location.href = `/login?redirect=${encodeURIComponent(window.location.pathname)}#${encodeURIComponent(window.location.hash)}`;
    }

    // Function to handle the logout button click
    const handleLogoutClick = async () => {
        // Call the apiLogout function to log the user out
        const response = await apiLogout({ authState, login, logout });
        if (response) {
            // If the logout is successful, redirect the user to the home page
            window.location.href = `/`;
        }
    }

    // Render the TopBar component
    return (
        <header className={styles['top-bar']}>
            <nav className={styles['left']}>
                {/* Left navigation section (currently empty) */}
            </nav>

            <nav className={styles['middle']}>
                {/* Middle navigation section with links to different pages */}
                <Link to={"/"}>Home</Link>
                <Link to={"/friends"}>Freunde</Link>
                <Link to={"/cook"}>Kochen</Link>
                <Link to={"/me"}>Profil</Link>
            </nav>

            <nav className={styles['right']}>
                {/* Right navigation section with login/logout button */}
                {authState && (
                    <button className={styles['logoutButton']} onClick={handleLogoutClick}>
                        Logout
                    </button>
                )}

                {!authState && (
                    <button className={styles['loginButton']} onClick={handleLoginClick}>
                        Login
                    </button>
                )}
            </nav>
        </header>
    );
}

export default TopBar;