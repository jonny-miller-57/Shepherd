import React from "react";
import BasicDatePicker from "./DatePicker";
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
import {DatePicker} from "@mui/x-date-pickers/DatePicker";

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

const onSubmit = (data: IFormInput) => {
    console.log(data)
}

interface IFormInput {
    date: Date,
    numClimbers: number,

}

function Home() {

    const today = new Date();

    const {
        handleSubmit,
        control,
        formState: { errors }
    } = useForm<IFormInput>({
        defaultValues: {
            numClimbers: 1,
            date: today,
        }
    });

    console.log("date: " + today);


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
                            Welcome!
                        </Typography>
                        <Box sx={{ mt: 1 }}>
                            <form onSubmit={handleSubmit(onSubmit)}>
                                <BasicDatePicker/>
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
                    </Box>
                </Grid>
            </Grid>
        </ThemeProvider>
    )
}

export default Home;