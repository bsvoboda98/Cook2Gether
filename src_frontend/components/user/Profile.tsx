import {useEffect, useState} from "react";
import {CurrentUser} from "../../types/user.types.ts";
import {getMe} from "../../api";
import {useNavigate} from "react-router-dom";

function Profile() {

    const [user, setUser] = useState<CurrentUser>();

    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserData = async () => {
            const response = await getMe();
            setUser(response);
        }
        fetchUserData();

    }, []);

    return(
        <>
            <h1>Deine Daten:</h1>
            <ul>
                <li>username: {user?.username}</li>
                <li>email: {user?.email}</li>
            </ul>
        </>
    )

}

export default Profile;