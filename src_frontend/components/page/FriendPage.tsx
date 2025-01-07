
import FriendAdd from "../user/FriendAdd.tsx";
import FriendRequests from "../user/FriendRequests.tsx";
import FriendList from "../user/FriendList.tsx";


function FriendPage() {

    return (
        <div>
            <div>
                <FriendList/>
            </div>
            <div>
                <FriendAdd/>
            </div>
            <div>
                <FriendRequests/>
            </div>
        </div>
    )

}

export default FriendPage;