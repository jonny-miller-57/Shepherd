import React, { useState, createContext, useContext } from 'react';
import { Box } from '@mui/material/'
import LogIn from "./LogIn"
import './App.css';
import SignUp from "./SignUp";

export const LoginContext = createContext({
    loggedIn: false,
    setLoggedIn: (loggedIn: boolean) => {}
});

function App() {
    const [loggedIn, setLoggedIn] = useState<boolean>(false);
    console.log(loggedIn);
    return (
        <div>
            <h1>{"User is logged in: " + loggedIn}</h1>
            <LogIn/>
        </div>
    );
}

export default App;