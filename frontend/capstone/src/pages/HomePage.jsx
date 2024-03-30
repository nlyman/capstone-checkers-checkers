import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import NavBarComponent from '../components/NavBarComponent';
import LoginPage from './LoginPage';
import { verifyLoginStatus, getLocalUsername } from '../functions/CookieFunctions';

const HomePage = () => {
    let [isLoggedIn, setIsLoggedIn] = useState(false);

    function fetchResults() {
        setIsLoggedIn(verifyLoginStatus())
    }
    useEffect(()=> {
        fetchResults();
    }, [isLoggedIn]);
    return <>
        {isLoggedIn?<>
            <NavBarComponent/>
        <p>Hello {getLocalUsername()}</p>
        </>: <>
            {<LoginPage/>}
            <br />
            <Link to="Register">Register</Link>
        </>}
    </>;
}

export default HomePage;