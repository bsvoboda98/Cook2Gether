import {useEffect, useState} from "react";
import {User} from "../../types/user.types.ts";
import {getFriends} from "../../api";



function FriendList() {
    const [friends, setFriends] = useState<User[]>([]);

    useEffect( () => {
        const fetchFriends = async() => {
            try{
                const response = await getFriends();
                setFriends(response);
            } catch (error) {
                console.error('Error', error);
            }

        }

        fetchFriends();
    }, []);

    return (
        <>
            <h2>Deine Freunde:</h2>
            <ul>
                {friends.map(friend => (
                    <li key={friend.id}>{friend.username}</li>
                ))}
            </ul>
        </>
    )
}
export default FriendList;
