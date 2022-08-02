import React, {createContext, useContext} from 'react';

export type GlobalContent = {
    loggedIn: boolean,
    setLoggedIn: (b: boolean) => void,
}

export const LoginContext = createContext<GlobalContent> ({
    loggedIn: false,
    setLoggedIn: () => {},
})

export const useLoginContext = () => useContext(LoginContext);