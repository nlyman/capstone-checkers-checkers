import axios from 'axios';
import { setLocalUsername, setIsLoggedIn } from '../functions/CookieFunctions';

const LoginComponent = () => {

    const manager = {
        login,
        logout
    };

    async function login(username, password){
        let dto={username: username, password: password};
        return checkLogin(dto).then(() => {
            setIsLoggedIn(true);
            setLocalUsername(username);
            return true;
        }).catch((error) => {
            console.error(error);
            return false;
        });
    }

    async function checkLogin(dto){
        return axios.post("/api/login", dto);
    }

    async function logout(){
        return checkLogout().then(() => {
            return true;
        }).catch((error) => {
            console.error(error);
            return false;
        });
    }

    async function checkLogout(){
        return axios.post("/api/logout");
    }

    return(manager);
}

export default LoginComponent;