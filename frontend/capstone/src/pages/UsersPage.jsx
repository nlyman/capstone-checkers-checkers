import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import NavBarComponent from '../components/NavBarComponent';
import { verifyLoginStatus } from '../functions/CookieFunctions';

const UsersPage = () => {
    let [users, setUsers] = useState([]);

    const navigate = useNavigate();
    if (!verifyLoginStatus()){
        navigate("/");
    }

    useEffect(() => {
        async function getUsers() {
            try{
                const response = await axios.get("/api/user/all");
                setUsers(response.data);
            }catch (error){
                console.error(`Error getting users: ${error}`);
            }
        }
        getUsers();
    }, []);

    async function challengeUser(name){
        try{
            const response = await axios.post(`/api/board/challenge-name/${name}`);
        await axios.get(`/api/board/join/${response.data}`);
        navigate("/game");
        }catch(error){
            console.log(error);
        }
    }

    return(<>
        {<NavBarComponent/>}
        {users.map((item, index) => (
            <div key={index} onClick={() => challengeUser(item)}>
                {item}
            </div>
        ))}
    </>);
}

export default UsersPage;