import {useEffect, useState} from "react";
import {acceptFriendRequest, getFriendRequests} from "../../api";
import {User} from "../../types/user.types.ts";

function FriendRequests() {
    const [users, setUsers] = useState<User[]>([]);

    useEffect(() => {
        const fetchFriendRequests = async () => {
            try{
                const response = await getFriendRequests();
                setUsers(response);
            } catch (error){
                console.error("Error while fetching FriendRequests", error);
            }
        }

        fetchFriendRequests();
    }, []);

    const handleAcceptClick = (id: number) => {
        acceptFriendRequest(id);
    }

    return(
        <>
            <h2>Eingehende Freundschaftsanfragen:</h2>
            <ul>
                {users.map(user => (
                    <li key={user.id}>{user.username} <button onClick={() => handleAcceptClick(user.id)}></button></li>
                ))}
            </ul>
        </>
    )




}

export default FriendRequests;
