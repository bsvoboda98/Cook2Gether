import axios, { AxiosInstance } from "axios";
import { refreshToken } from "./endpoints/auth.ts";

// Base URL for the API, retrieved from environment variables
const BASE_URL = import.meta.env.VITE_API_URL;

// Create an Axios instance with the base URL
const apiClient: AxiosInstance = axios.create({
    baseURL: BASE_URL,
});

// Request interceptor to add authentication token to requests
apiClient.interceptors.request.use(function (config) {
    // Retrieve the JWT token from local storage
    let token = localStorage.getItem("jwt-token");

    // Regular expression to check if the URL does not start with 'auth'
    const needsAuthentication = new RegExp('^\/(?!auth)');

    // If a token exists and the request URL needs authentication, add the token to the headers
    if (token && needsAuthentication.test(config.url)) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }

    // Return the modified config
    return config;
}, function (error) {
    // Return the error if the request fails
    return Promise.reject(error);
});

// Response interceptor to handle authentication and refresh token
apiClient.interceptors.response.use(
    response => response, // Pass through successful responses
    async (error) => {
        const prevRequest = error?.config; // Get the previous request configuration

        // Check if the response status is 403 (Forbidden), the request has not been sent yet,
        // and the request URL is not for refreshing the token or checking login
        if (error?.response?.status == 403
            && !prevRequest?.sent
            && !prevRequest.url.toLowerCase().includes('refreshtoken')
            && !prevRequest.url.toLowerCase().includes('checklogin')) {

            // Mark the request as sent to avoid infinite loops
            prevRequest.sent = true;

            // Refresh the access token
            const newAccessToken = await refreshToken();

            // Update the request headers with the new access token
            prevRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;

            // Retry the request with the new token
            return apiClient(prevRequest);
        }

        // If the response status is 403 and the request URL is for refreshing the token,
        // redirect the user to the login page with the current path and hash as query parameters
        if (error?.response?.status == 403 && prevRequest.url.toLowerCase().includes('refreshtoken')) {
            // Construct the redirect URL with query parameters
            window.location.href = `/login?redirect=${encodeURIComponent(window.location.pathname)}#${encodeURIComponent(window.location.hash)}`;
        }

        // Return the error if the response is not handled
        return Promise.reject(error);
    }
);

// Export the configured API client
export default apiClient;