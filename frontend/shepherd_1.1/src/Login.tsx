import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import axios from 'axios';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Image from './shepherd.jpeg';
import { cyan, indigo } from "@mui/material/colors";
import { SubmitHandler, useForm, Controller } from "react-hook-form";
import * as yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import {useContext} from "react";
import {LoginContext} from "./LoginContext";
import {Link} from "@mui/material";

const theme = createTheme({
    palette: {
        primary: {
            main: cyan["500"]
        },
        secondary: {
            main: indigo["500"]
        },
    }
});

interface IFormInput {
    username: string;
    password: string;
}

const validationSchema = yup.object().shape({
    username: yup
        .string()
        .required("This is required"),
    password: yup
        .string()
        .required("This is required")
        .min(6, "Password must be between 6-20 characters")
        .max(20, "Password must be between 6-20 characters"),
})

export default function Login() {
    const {  switchToSignup, switchToHome } = useContext(LoginContext);

    const {
        handleSubmit,
        control,
        formState: { errors }
    } = useForm<IFormInput>({
        resolver: yupResolver(validationSchema),
        defaultValues: {
            username: "",
            password: "",
        }
    });



    const onSubmit: SubmitHandler<IFormInput> = async (data) => {
        const axios = require("axios");
        console.log("data prior to get request: " + JSON.stringify(data));
        axios
            .get(`http://localhost:4567/login?user=${data.username}&pass=${data.password}`)
            .then((resp: { data: IFormInput; }) => {
                console.log("data: " + JSON.stringify(resp.data));
                switchToHome;
            })
            .catch((errors: any) => {
                console.log("errors: " + errors);
            });
    }


    return (
        <ThemeProvider theme={theme}>
            <Grid container component="main" sx={{ height: '100vh' }}>
                <CssBaseline />
                <Grid
                    item
                    xs={false}
                    sm={4}
                    md={7}
                    sx={{
                        backgroundImage: `url(${Image})`,
                        backgroundColor: (t) =>
                            t.palette.mode === 'light' ? t.palette.grey[50] : t.palette.grey[900],
                        backgroundSize: 'cover',
                        backgroundPosition: 'center',
                    }}
                />
                <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
                    <Box
                        sx={{
                            my: 8,
                            mx: 4,
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                        }}
                    >
                        <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}/>
                        <Typography component="h1" variant="h5">
                            Log in
                        </Typography>
                        <Box sx={{ mt: 1 }}>
                            <form onSubmit={handleSubmit(onSubmit)}>
                                <Controller
                                    name={"username"}
                                    control={control}
                                    render={({ field }) => (
                                        <TextField
                                            {...field}
                                            margin="normal"
                                            required
                                            fullWidth
                                            id="username"
                                            label="Username"
                                            name="username"
                                            error={!!errors.username}
                                            helperText={errors.username ? errors.username.message : ""}
                                        />
                                    )}
                                />
                                <Controller
                                    name={"password"}
                                    control={control}
                                    render={({ field }) => (
                                        <TextField
                                            {...field}
                                            margin="normal"
                                            required
                                            fullWidth
                                            name="password"
                                            label="Password"
                                            type="password"
                                            id="password"
                                            error={!!errors.password}
                                            helperText={errors.password ? errors.password.message : ""}
                                        />
                                    )}
                                />
                                <Button
                                    type="submit"
                                    fullWidth
                                    variant="contained"
                                    color={"primary"}
                                    sx={{ mt: 3, mb: 2 }}
                                >
                                    Log In
                                </Button>
                            </form>
                        </Box>
                        <Grid item>
                            <Link
                                variant="body2"
                                onClick={switchToSignup}
                            >
                                {"Don't have an account? Sign Up"}
                            </Link>
                        </Grid>
                    </Box>
                </Grid>
            </Grid>
        </ThemeProvider>
    );
}