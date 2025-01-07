import  { createContext, useState } from "react";

interface AuthContextInterface {
    authState: string;
    login: () => void;
    logout: () => void;
}
const AuthContext = createContext<AuthContextInterface>({
    authState: "",
    login: () => {},
    logout:() => {},
});

const AuthProvider: React.FC = ({children}) => {
    const [authState, setAuthState] = useState<boolean>(false);

    const login = () => {
        setAuthState(true);
    };

    const logout = () => {
        setAuthState(false);
    };

    return(
        <AuthContext.Provider value={{authState, login, logout}}>
            {children}
        </AuthContext.Provider>
    );
};

export {AuthContext, AuthProvider};