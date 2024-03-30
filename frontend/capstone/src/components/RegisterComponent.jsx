import axios from 'axios';

const RegisterComponent = () => {
    
    async function handleRegister (username, password) {
        return await axios.post("/api/user",
            {username: username, password: password},
            {headers: { 'Accept': 'application/json'}}
        );
    }

    return(handleRegister);
}

export default RegisterComponent;