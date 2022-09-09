import React, { useState } from 'react';
import Login from "./Login"
import './App.css';
import {Profile} from "./Profile";
import {LoginContext} from "./LoginContext";
import SignUp from "./SignUp";
import Home from "./Home";

function App() {
    const [active, setActive] = useState<string>("loggedin");
    const [user, setUser] = useState<Profile | null>(null);

    const switchToSignup = () => {
        setActive("signup");
    }

    const switchToLogin = () => {
        setActive("login");
    }

    const switchToHome = () => {
        setActive("loggedin")
    }

    const contextValue = { switchToLogin, switchToSignup, switchToHome };

    return (
        <LoginContext.Provider value={contextValue}>
            {active === "login" && <Login/>}
            {active === "signup" && <SignUp/>}
            {active === "loggedin" && <Home/>}
        </LoginContext.Provider>
    );
}

export default App;