import apiClient from "../apiClient.ts";
import {AuthContext} from "../../utils/authContext.tsx";

interface LoginData {
    email: string;
    password: string;
}

interface SignupData {
    username: string;
    email: string;
    password: string;
}

export const verify = async (authContext: AuthContext, loginData: LoginData, cancel: AbortController): Promise<{success: boolean, data?: any; error?: string}> => {
    try{
        const response = await apiClient.post('/auth/login', loginData, {
            withCredentials: true,
            signal: cancel.signal
        });
        localStorage.clear();
        localStorage.setItem("jwt-token", response.data.token);
        localStorage.setItem("expires", Date.now() + response.data.expires);
        localStorage.setItem("duration", response.data.expires);

        authContext.login();
        return {success: true, data: response.data};
    } catch (error) {
        console.error('Error during login', error);

        if(error.response && error.response.status === 401){
            return {success: false, error: 'Invalid credentials'};
        }

        return {success: false, error: 'An error occurred during login'};
    }
}

export const signup = async (authContext: AuthContext, formData: SignupData): Promise<{success: boolean, data?: any, error?: string}> => {
    try{
        const response = await apiClient.post('/auth/signup', formData);
        localStorage.clear();
        localStorage.setItem("jwt-token", response.data.token);
        localStorage.setItem("expires", Date.now() + response.data.expires);
        localStorage.setItem("duration", response.data.expires);
        authContext.login();
        return {success: true, data: response.data};
    } catch (error) {

        if(error.response && error.response.status === 409){
            console.error(error.response.data.detail);
            return {success: false, error: error.response.data.detail}
        }
        const message = 'Error during signup';
        console.error(message, error);
        return {success: false, error: message};
    }
}

export const refreshToken = async () => {

    try{
        const response = await apiClient.get('auth/refreshToken', {
            withCredentials: true
        });
        localStorage.clear();
        localStorage.setItem("jwt-token", response.data.token);
        localStorage.setItem("expires", Date.now() + response.data.expires);
        localStorage.setItem("duration", response.data.expires);

    } catch (error) {
        console.error("Error while refreshing Token", error);
    }

}

export const checkLogin = async (authContext: AuthContext)=> {
    try{
        const response = await apiClient.get('auth/checkLogin', {
            withCredentials: true
        });
        localStorage.clear();
        localStorage.setItem("jwt-token", response.data.token);
        localStorage.setItem("expires", Date.now() + response.data.expires);
        localStorage.setItem("duration", response.data.expires);
        authContext.login();
    } catch (error) {
        authContext.logout();
    }
}

export const apiLogout = async (authContext: AuthContext):Promise<boolean> => {
    try{
        await apiClient.get('/auth/logout', {
            withCredentials: true
        }).then(() => {
            authContext.logout();
            localStorage.clear();
        })
        return true;

    } catch (error) {
        console.error(error);
        return false;
    }
}