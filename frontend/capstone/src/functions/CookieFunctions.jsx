import axios from 'axios';

function getCookie(cookieName) {
    const name = cookieName + "=";
    const cookies = decodeURIComponent(document.cookie).split(';');

    for (let i=0; i < cookies.length; i++) {
        let cookie = cookies[i].trim();
        if (cookie.startsWith(name)) {
            return cookie.substring(name.length);
        }
    }
    return null;
}

export function getSessionId(){
    const result=getCookie("user-id");
    if (result===null){
        return 0;
    }
    return BigInt(result);
}

export function setLocalUsername(username){
    localStorage.setItem('username', username);
}

export function getLocalUsername(){
    return localStorage.getItem('username');
}

export function setIsLoggedIn(yes){
    localStorage.setItem("isLoggedIn", yes);
}

export function verifyLoginStatus(){
    if (localStorage.getItem('isLoggedIn')!=='true'){
        return false;
    }
    checkServerIfLoginValid().then(() => {
        
    }).catch(() => {
        axios.delete("/api/clear");
        setIsLoggedIn(false);
        setLocalUsername("");
        window.location.reload();
    });
    return true;
}

async function checkServerIfLoginValid(){
    return axios.get("/api/check");
}