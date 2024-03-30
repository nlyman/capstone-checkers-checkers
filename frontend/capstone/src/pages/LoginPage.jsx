import { useState } from 'react';
import LoginComponent from '../components/LoginComponent';

const LoginPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const manager = LoginComponent();

    function handleLogin(){
        manager.login(username, password).then((result) => {
            if (result){
                window.location.reload();
            }
        })
    }

    return(<>
        <div>
        <h2>Login</h2>
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
                <button type="button" onClick={handleLogin}>Login</button>
            </form>
        </div>
    </>);
}

export default LoginPage;