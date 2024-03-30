import { useState } from 'react';
import RegisterComponent from '../components/RegisterComponent';
import { useNavigate } from 'react-router-dom';

const RegisterPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const register=RegisterComponent();

    function handleSubmit () {
        register(username, password).then(() => {
            navigate("/");
        }).catch((error) => {
            if (error.status==409){
                setError(`${username} is taken.`);
            }
            else{
                setError("Registration failed.");
            }
        })
    }

    function backToHome(){
        navigate("/");
    }

    return(
        <>
            <h2>Create account</h2>
            <label>{error}</label>
            <div>
                <form>
                    Username: <input
                        type="text"
                        id="username"
                        name="username"
                        placeholder="Enter username"
                        onChange={(e) => setUsername(e.target.value)}
                    /><br />
                    Password: <input
                        type="password"
                        id="password"
                        name="password"
                        placeholder="Enter password"
                        onChange={(e) => setPassword(e.target.value)}
                    /><br />
                    <button type="button" onClick={handleSubmit}>Submit</button>
                    <button type="button" onClick={backToHome}>Cancel</button>
                </form>
            </div>
        </>
    );
}

export default RegisterPage;