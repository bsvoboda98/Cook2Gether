import {useState, useEffect} from "react";
import Autosuggest from "react-autosuggest";
import {searchUsers, sendFriendRequest} from "../../api";




function FriendAdd() {
    const [users, setUsers] = useState([]);
    const [value, setValue] = useState('');

    useEffect(() => {
        const timer = setTimeout(async () => {
            if ( value.length > 2) {
                try{
                    const response = await searchUsers(value);
                    setUsers(response);
                } catch (error) {
                    console.error("Error", error);
                }
            }
        }, 300);
        return () => clearTimeout(timer);
    }, [value]);

    const onChange = (_, {newValue}) => {
        setValue(newValue);
    };

    const handleAddClick = (id: number) => {
        try{
            sendFriendRequest(id);
        } catch (error) {
            console.error("Error while sinding friendRequest", error);
        }
    }

    const renderSuggestion = (suggestion) => (
        <div>{suggestion.username} <button onClick={() => handleAddClick(suggestion.id)}>add</button></div>
    );

    const inputProps = {
        placeholder: 'Search for users',
        value,
        onChange,
        type: 'text'
    };

    return(
        <>
            <h2>Freunde hinzufügen:</h2>
            <Autosuggest
                suggestions={users}
                onSuggestionsFetchRequested={() => {}}
                onSuggestionsClearRequested={() => setUsers([])}
                getSuggestionValue={(suggestion) => suggestion.username}
                renderSuggestion={renderSuggestion}
                inputProps={{
                    ...inputProps,
                    onChange: onChange,
                }}
            />
        </>

    )


}

export default FriendAdd