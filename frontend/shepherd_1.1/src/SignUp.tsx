import * as React from 'react';
import { SubmitHandler, useForm, Controller } from "react-hook-form";
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import * as yup from "yup";
import { yupResolver } from '@hookform/resolvers/yup';
import { MenuItem} from "@mui/material";
import {cyan, indigo} from "@mui/material/colors";

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

const genders: string[] = [
    "Other", "Non-Binary", "Female", "Male",
]

interface IFormInput {
    username: string;
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    confirmPassword: string;
    flashGrade: number;
    projectGrade: number;
    heightFeet: number;
    heightInches: number;
    apeIndex: number;
    gender: string;
}

const validationSchema = yup.object().shape({
    username: yup
        .string()
        .required("This is required"),
    firstName: yup
        .string()
        .required("This is required"),
    lastName: yup
        .string()
        .required("This is required"),
    email: yup
        .string()
        .email()
        .required("This is required")
        .matches(/^[A-Z\d._%+-]+@[A-Z\d.-]+\.[A-Z]{2,}$/i, "Invalid email address"),
    password: yup
        .string()
        .required("This is required")
        .min(6, "Password must be between 6-20 characters")
        .max(20, "Password must be between 6-20 characters"),
    confirmPassword: yup
        .string()
        .required("This is required")
        .oneOf([yup.ref('password'), null], "Passwords must match"),
    flashGrade: yup
        .number()
        .integer()
        .required("This is required"),
    projectGrade: yup
        .number()
        .integer()
        .required("This is required")
        .moreThan(yup.ref("flashGrade"), "Project Grade must be higher than Flash Grade"),
    heightFeet: yup
        .number()
        .integer()
        .required("This is required"),
    heightInches: yup
        .number()
        .integer()
        .required("This is required"),
    apeIndex: yup
        .number()
        .integer()
        .required("This is required"),
});

const grades: JSX.Element[] = [];
for (let i = 0; i < 17; i++) {
    grades.push(
        <MenuItem key={i} value={i}>{"V" + i}</MenuItem>
    )
}

const genderSelections : JSX.Element[] = [];
for (const i in genders) {
    genderSelections.push(
        <MenuItem key={i} value={genders[i]}>{genders[i]}</MenuItem>
    )
}

const feetSelections : JSX.Element[] = [];
for (let i = 0; i < 9; i++) {
    feetSelections.push(
        <MenuItem key={i} value={i}>{i}</MenuItem>
    )
}

const inchesSelections : JSX.Element[] = [];
for (let i = 0; i < 12; i++) {
    inchesSelections.push(
        <MenuItem key={i} value={i}>{i}</MenuItem>
    )
}

export default function SignUp() {
    const {
        handleSubmit,
        control,
        formState: {errors}
    } = useForm<IFormInput>({
        resolver: yupResolver(validationSchema),
        defaultValues: {
            username: "",
            firstName: "",
            lastName: "",
            email: "",
            password: "",
            confirmPassword: "",
            flashGrade: 0,
            projectGrade: 1,
            heightFeet: 5,
            heightInches: 0,
            apeIndex: 0,
            gender: "",
        }
    });

    const onSubmit: SubmitHandler<IFormInput> = data => console.log(data);
    // const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    //     event.preventDefault();
    //     const data = new FormData(event.currentTarget);
    //     console.log({
    //         email: data.get('email'),
    //         password: data.get('password'),
    //     });
    // };
    return (
        <ThemeProvider theme={theme}>
            <Container component="main" maxWidth="xs">
                <CssBaseline />
                <Box
                    sx={{
                        marginTop: 8,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}
                >
                    <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}/>
                    <Typography component="h1" variant="h5">
                        Sign up
                    </Typography>
                    <Box sx={{ mt: 3 }}>
                        <form onSubmit={handleSubmit(onSubmit)}>
                            <Grid container spacing={2}>
                                <Grid item xs={12} sm={6}>
                                    <Controller
                                        name={"firstName"}
                                        control={control}
                                        render={({ field }) => (
                                            <TextField
                                                {...field}
                                                required
                                                fullWidth
                                                id={"firstName"}
                                                label={"First Name"}
                                                variant={"outlined"}
                                                error={!!errors.firstName}
                                                helperText={errors.firstName ? errors.firstName.message : ""}
                                            />
                                        )}
                                    />
                                </Grid>
                                <Grid item xs={12} sm={6}>
                                    <Controller
                                        name={"lastName"}
                                        control={control}
                                        render={({ field }) => (
                                            <TextField
                                                {...field}
                                                required
                                                fullWidth
                                                id={"lastName"}
                                                label={"Last Name"}
                                                variant={"outlined"}
                                                error={!!errors.lastName}
                                                helperText={errors.lastName ? errors.lastName.message : ""}
                                            />
                                        )}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <Controller
                                        name={"gender"}
                                        control={control}
                                        render={({ field }) => (
                                            <TextField
                                                {...field}
                                                required
                                                fullWidth
                                                select
                                                id={"gender"}
                                                label={"Gender"}
                                                variant={"outlined"}
                                                error={!!errors.gender}
                                                helperText={errors.gender ? errors.gender.message : ""}
                                            >
                                                {genderSelections}
                                            </TextField>
                                        )}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <Controller
                                        name={"email"}
                                        control={control}
                                        render={({ field }) => (
                                            <TextField
                                                {...field}
                                                required
                                                fullWidth
                                                label={"Email Address"}
                                                type={"email"}
                                                variant={"outlined"}
                                                error={!!errors.email}
                                                helperText={errors.email ? errors.email.message : ""}
                                            />
                                        )}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <Controller
                                        name={"username"}
                                        control={control}
                                        render={({ field }) => (
                                            <TextField
                                                {...field}
                                                fullWidth
                                                required
                                                id={"username"}
                                                label={"Create New Username"}
                                                variant={"outlined"}
                                                error={!!errors.username}
                                                helperText={errors.username ? errors.username.message : ""}
                                            />
                                        )}
                                    />
                                </Grid>
                                <Grid item xs={12} sm={6}>
                                    <Controller
                                        name={"password"}
                                        control={control}
                                        render={({ field }) => (
                                            <TextField
                                                {...field}
                                                required
                                                fullWidth
                                                id={"password"}
                                                label={"Create Password"}
                                                type={"password"}
                                                variant={"outlined"}
                                                error={!!errors.password}
                                                helperText={errors.password ? errors.password.message : ""}
                                            />
                                        )}
                                    />
                                </Grid>
                                <Grid item xs={12} sm={6}>
                                    <Controller
                                        name={"confirmPassword"}
                                        control={control}
                                        render={({ field }) => (
                                            <TextField
                                                {...field}
                                                required
                                                fullWidth
                                                id={"confirmPassword"}
                                                label={"Confirm Password"}
                                                type={"password"}
                                                variant={"outlined"}
                                                error={!!errors.confirmPassword}
                                                helperText={errors.confirmPassword ? errors.confirmPassword.message : ""}
                                            />
                                        )}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <Controller
                                        name={"flashGrade"}
                                        control={control}
                                        render={({ field }) => (
                                            <TextField
                                                {...field}
                                                required
                                                fullWidth
                                                select
                                                id={"flashGrade"}
                                                label={"Flash Grade"}
                                                variant={"outlined"}
                                                error={!!errors.flashGrade}
                                                helperText={errors.flashGrade ? errors.flashGrade.message : ""}
                                            >
                                                {grades}
                                            </TextField>
                                        )}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <Controller
                                        name={"projectGrade"}
                                        control={control}
                                        render={({ field }) => (
                                            <TextField
                                                {...field}
                                                required
                                                fullWidth
                                                select
                                                id={"projectGrade"}
                                                label={"Project Grade"}
                                                variant={"outlined"}
                                                error={!!errors.projectGrade}
                                                helperText={errors.projectGrade ? errors.projectGrade.message : ""}
                                            >
                                                {grades}
                                            </TextField>
                                        )}
                                    />
                                </Grid>
                                <Grid item xs={12} sm={6}>
                                    <Controller
                                        name={"heightFeet"}
                                        control={control}
                                        render={({ field }) => (
                                            <TextField
                                                {...field}
                                                required
                                                fullWidth
                                                select
                                                id={"heightFeet"}
                                                label={"Height"}
                                                variant={"outlined"}
                                                error={!!errors.heightFeet}
                                                helperText={errors.heightFeet ? errors.heightFeet.message : "Feet"}
                                            >
                                                {feetSelections}
                                            </TextField>
                                        )}
                                    />
                                </Grid>
                                <Grid item xs={12} sm={6}>
                                    <Controller
                                        name={"heightInches"}
                                        control={control}
                                        render={({ field }) => (
                                            <TextField
                                                {...field}
                                                required
                                                fullWidth
                                                select
                                                id={"heightInches"}
                                                label={"Height"}
                                                variant={"outlined"}
                                                error={!!errors.heightInches}
                                                helperText={errors.heightInches ? errors.heightInches.message : "Inches"}
                                            >
                                                {inchesSelections}
                                            </TextField>
                                        )}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <Controller
                                        name={"apeIndex"}
                                        control={control}
                                        render={({ field }) => (
                                            <TextField
                                                {...field}
                                                required
                                                fullWidth
                                                id={"apeIndex"}
                                                label={"Ape Index"}
                                                type={"number"}
                                                variant={"outlined"}
                                                error={!!errors.apeIndex}
                                                helperText={errors.apeIndex ? errors.apeIndex.message : ""}
                                                InputLabelProps={{
                                                    shrink: true,
                                                }}
                                            />
                                        )}
                                    />
                                </Grid>
                            </Grid>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2 }}
                            >
                                Sign Up
                            </Button>
                            <Grid container justifyContent="flex-end">
                                <Grid item>
                                    <Link href="#" variant="body2">
                                        Already have an account? Sign in
                                    </Link>
                                </Grid>
                            </Grid>
                        </form>
                    </Box>
                </Box>
            </Container>
        </ThemeProvider>
    );
}