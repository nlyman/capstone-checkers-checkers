import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './pages/HomePage';
import BoardPage from './pages/BoardPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import LobbyPage from './pages/LobbyPage';
import UsersPage from './pages/UsersPage';

function App() {

  return (<>
    <h1>Checkers By Nathan Lyman</h1>
    <Router>
        <Routes>
          <Route path = "/" element = {<HomePage />} />
          <Route path = "/game" element = {<BoardPage />} />
          <Route path = "/login" element = {<LoginPage />} />
          <Route path = "/register" element = {<RegisterPage />} />
          <Route path = "/lobbies" element = {<LobbyPage />} />
          <Route path = "/view-users" element = {<UsersPage />} />
        </Routes>
    </Router>
    </>
  )
}

export default App
