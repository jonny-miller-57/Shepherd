import React, { Component } from 'react';
import { Box } from '@mui/material/'
import SignUp from "./SignUp";
import SignIn from "./SignIn"
import './App.css';

interface AppState{
    loggedIn: boolean
}

class App extends Component<{}, AppState> {
    constructor(props: {}) {
        super(props);
        this.state = {loggedIn: false};
    }
    render() {
        if (!this.state.loggedIn) {
            return (
                <Box>
                    <SignIn/>
                </Box>
            )
        } else {
            return (
                <Box>
                    <SignUp/>
                </Box>
            )
        }
    }
}

export default App;