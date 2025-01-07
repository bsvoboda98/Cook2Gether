import {BrowserRouter as Router, Routes, Route} from "react-router-dom";

import Profile from "./components/user/Profile.tsx";
import FriendPage from "./components/page/FriendPage.tsx";
import TopBar from "./components/bar/TopBar/TopBar.tsx";
import LoginPage from "./components/page/LoginPage/LoginPage.tsx";
import RegistrationPage from "./components/page/RegistrationPage.tsx";
import RecipeMenu from "./components/page/RecipeMenu/RecipeMenu.tsx";
import CreateRecipeForm from "./components/recipe/CreateRecipeForm.tsx";
import DisplayRandomRecipe from "./components/page/RecipePage/DisplayRandomRecipe.tsx";
import HomePage from "./components/page/HomePage.tsx";
import DisplayRecipeById from "./components/page/RecipePage/DisplayRecipeById.tsx";
import {AuthProvider} from "./utils/authContext.tsx";
function App() {
    return (
        <AuthProvider>
            <Router>
                <TopBar/>
                <Routes>
                    <Route path={"/"} element={<HomePage/>}/>
                    <Route path={"/login"} element={<LoginPage/>}/>
                    <Route path={"/register"} element={<RegistrationPage/>}/>
                    <Route path={"/home"} element={<Profile/>}/>
                    <Route path={"/me"} element={<Profile/>}/>
                    <Route path={"/friends"} element={<FriendPage/>}/>
                    <Route path={"/cook"} element={<RecipeMenu/>}/>
                    <Route path={"/cook/test"} element={<DisplayRandomRecipe/>}/>
                    <Route path={"/cook/search"} element={<RecipeMenu/>}/>
                    <Route path={"/cook/create"} element={<CreateRecipeForm/>}/>
                    <Route path={"/recipe/:id"} element={<DisplayRecipeById/>}/>
                </Routes>
            </Router>
        </AuthProvider>
    )
}

export default App
