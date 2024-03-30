import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import NavBarComponent from '../components/NavBarComponent';
import { verifyLoginStatus, getLocalUsername } from '../functions/CookieFunctions';

const LobbyPage = () => {
    let [rooms, setRooms] = useState([]);

    const navigate = useNavigate();
    if (!verifyLoginStatus()){
        navigate("/");
    }

    useEffect(() => {
        async function getRooms(){
            try{
                const response = await axios.get("/api/board/rooms");
                setRooms(response.data);
            }catch (error){
                console.error(`Error getting rooms: ${error}`);
            }
        }
        getRooms();
    }, []);

    async function joinRoom(roomId){
        try{
            axios.get(`/api/board/join/${roomId}`).then(() => {
                navigate("/game");
            });
        }catch (error){
            console.log(error);
        }
    }

    function readStatus(item){
        switch(item.status){
            case 0:
                return 'Ongoing';
            case 1:
                return 'Draw';
            case 2:
                if (item.youPlayerOne){
                    return getLocalUsername();
                }
                else{
                    return item.otherPlayerName;
                }
            case 3:
                if (item.youPlayerOne){
                    return item.otherPlayerName;
                }
                else{
                    return getLocalUsername();
                }
        }
    }

    return(<>
        {NavBarComponent()}
        <table>
            <tbody>
                {rooms.map((item, index) => (
                    <row key={index} onClick={() => joinRoom(item.id)}>
                        <td>{item.otherPlayerName}</td>
                        <td>{readStatus(item)}</td>
                    </row>
                ))}
            </tbody>
        </table>
    </>);
}

export default LobbyPage;