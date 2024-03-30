import { useNavigate } from 'react-router-dom';
import LoginComponent from './LoginComponent';

const NavBarComponent = () => {
    const navigate = useNavigate();
    function createButton(content, execute){
        return(<button onClick={() => execute()}>{content}</button>);
    }

    function logout(){
        const manager = LoginComponent();
        manager.logout().then((success) => {
            if (success){
                navigate("/");
            }
        });
    }
    
    function navLobbies(){
        navigate("/lobbies");
    }
    
    function navUsers(){
        navigate("/view-users");
    }

    function navGame(){
        navigate("/game");
    }

    function navHome(){
        navigate("/");
    }

    return(<>
        {createButton("Home", navHome)}
        {createButton("Challenge", navUsers)}
        {createButton("Lobbies", navLobbies)}
        {createButton("Play", navGame)}
        {createButton("Logout", logout)}
        <br />
    </>);
}

export default NavBarComponent;