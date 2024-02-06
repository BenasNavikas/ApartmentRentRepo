import React, { FC, ReactElement, useEffect, useState } from 'react';
import {
    Paper,
    Typography,
    Box,
    Button,
    TextField,
    Divider,
    MenuItem,
    Select,
    FormHelperText,
} from '@mui/material';
import { LoadingButton } from '@mui/lab';
import Navbar from '../../Components/Navbar/navbar';
import Footer from '../../Components/Footer/footer';
import { IPage } from '../../shared/models/Page';
import useStyles from '../../Components/Styles/global-styles';
import { usePost, useGet } from '../../services/api-service';
import { useNavigate } from 'react-router-dom';
import useErrorHandling from '../../services/handle-responses';
import { ILesseeDTO } from '../../shared/dtos/LesseeDTO';
import { ILessor } from '../../shared/models/Lessor';

const LesseeCreationFormPage: FC<IPage> = ({ openSnackbar }): ReactElement => {
    const classes = useStyles('light');
    const navigate = useNavigate();
    const { handleErrorResponse } = useErrorHandling();
    const [selectedLessor, setSelectedLessor] = useState<{ id: string } | null>(null);
    const [creationCompleted, setCreationCompleted] = useState(false);

    const [form, setForm] = useState<Record<string, any>>({
        name: { value: '', errorMessage: '' },
        address: { value: '', errorMessage: '' },
        phoneNumber: { value: '', errorMessage: '' },
        email: { value: '', errorMessage: '' },
        accountNumber: { value: '', errorMessage: '' },
        username: { value: '', errorMessage: '' },
        lessorId: { value: '', errorMessage: '' },
    });
    const { data: lessors, error: lessorsError } = useGet<ILessor[]>('lessors', {});

    const { data, error, postData } = usePost<ILesseeDTO>(`lessees/${form.lessorId.value}`, {});



    useEffect(() => {
        handleAPIError(error, openSnackbar);
    }, [error, openSnackbar]);

    useEffect(() => {
        if (data !== null && creationCompleted) {
            openSnackbar('Lessee created successfully', 'success');
            navigate(-1);
        }
    }, [data, creationCompleted, openSnackbar, navigate]);

    const handleAPIError = (error: any, snackbar: any) => {
        if (error && [401, 403].includes(error.statusCode)) {
            handleErrorResponse(error.statusCode);
            snackbar(error.message, 'error');
        } else if (error?.description.includes("Refresh Token")) {
            navigate("/login");
            openSnackbar("You need to login again", 'warning');
        } else if (error && error.statusCode === 422) {
            const updatedForm = { ...form };
            const fieldErrors = JSON.parse(error.description);
            Object.keys(fieldErrors).forEach((field) => {
                if (updatedForm[field]) {
                    updatedForm[field].errorMessage = fieldErrors[field];
                }
            });
            setForm(updatedForm);
        } else if (error) {
            snackbar(error.message, 'error');
        }
    };

    const handleChange = <T extends keyof typeof form>(name: T, value: typeof form[T]['value']): void => {
        setForm((prevForm) => ({
            ...prevForm,
            [name]: {
                value,
                errorMessage: '',
            },
        }));
    };

    const handleSaveChanges = async (
        e: React.MouseEvent<HTMLElement>
    ): Promise<void> => {
        e.preventDefault();

        // Extract lessorId from the selected lessor name
        const selectedLessorName = form.lessorId.value;
        // const selectedLessor2 = (lessors || []).find((lessor) => lessor.name === selectedLessorName);
        setSelectedLessor(selectedLessorName)
        console.log("SeleLessor: " + selectedLessorName);
        // if (!selectedLessor) {
        //     // Handle the case where the selected lessor is not found
        //     // You can show an error message or handle it according to your requirements
        //     return;
        // }

        // Create a copy of the form data excluding lessorId
        const { lessorId, ...formDataWithoutLessorId } = form;

        const request: ILesseeDTO = Object.keys(formDataWithoutLessorId).reduce(
            (acc, key) => ({ ...acc, [key]: formDataWithoutLessorId[key].value }),
            {}
        );
        console.log("selected: " + selectedLessor?.id);

        // Use the selected lessor's id in the endpoint
        await postData(request); // Replace with your postData implementation

        setCreationCompleted(true);
    };

    return (
        <Box
            className={classes.body}
            sx={{
                flexGrow: 1,
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'space-between',
                minHeight: '100vh',
                height: '100%',
                overflow: 'hidden',
                backgroundColor: '#8FBC8F',
            }}
        >
            <Navbar title="RentEase" />
            <Paper className={classes.paper} elevation={16} square>
                <Box>
                    <Typography variant="h5" color="black" align="center" marginTop={2}>
                        Lessee Profile Creation
                    </Typography>
                    <Divider />
                </Box>
                <Box className={classes.form}>
                    {error !== null && error.statusCode !== 422 && (
                        <Box
                            sx={{
                                flexGrow: 1,
                                display: 'flex',
                                width: '100%',
                                justifyContent: 'center',
                            }}
                        >
                            <Typography variant="body1" color="red">
                                {error.statusCode === 401 ? 'Bad Credentials' : error.description}
                            </Typography>
                        </Box>
                    )}
                    {Object.entries(form).map(([key, { value, errorMessage }]) => (
                        key === 'lessorId' ? (
                            <React.Fragment key={key}>
                                <Select
                                    key={key}
                                    label={key
                                        .split(/(?=[A-Z])/)
                                        .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
                                        .join(' ')}
                                    name={key}
                                    value={value}
                                    onChange={(e) => handleChange(key as keyof typeof form, e.target.value as typeof form['lessorId']['value'])}
                                    error={errorMessage !== ''}
                                    size="small"
                                    margin="dense"
                                    required
                                    sx={{ width: '33%', height: '100%' }}
                                >
                                    {(lessors || []).map((lessor) => (
                                        <MenuItem key={lessor.id} value={lessor.id} sx={{ height: '100%' }}>
                                            {lessor.name}
                                        </MenuItem>
                                    ))}
                                </Select>
                                {errorMessage && (
                                    <FormHelperText error>
                                        {errorMessage}
                                    </FormHelperText>
                                )}
                            </React.Fragment>
                        ) : (
                            <TextField
                                key={key}
                                id={key}
                                label={key
                                    .split(/(?=[A-Z])/)
                                    .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
                                    .join(' ')}
                                name={key}
                                type={key}
                                value={value}
                                onChange={(e) => handleChange(key as keyof typeof form, e.target.value)}
                                error={errorMessage !== ''}
                                helperText={errorMessage}
                                size="small"
                                margin="normal"
                                required
                            />
                        )
                    ))}
                    <Box
                        sx={{
                            flexGrow: 1,
                            display: 'flex',
                            width: '100%',
                            paddingTop: '16px',
                            justifyContent: 'center',
                        }}
                    >
                        <LoadingButton
                            size="medium"
                            color="inherit"
                            variant="outlined"
                            onClick={handleSaveChanges}
                            sx={{
                                marginRight: '8px',
                            }}
                        >
                            Save
                        </LoadingButton>
                        <Button
                            size="medium"
                            color="inherit"
                            variant="outlined"
                            onClick={(): void => {
                                navigate(-1);
                            }}
                        >
                            Cancel
                        </Button>
                    </Box>
                </Box>
            </Paper>
            <Footer />
        </Box>
    );
};

export default LesseeCreationFormPage;
