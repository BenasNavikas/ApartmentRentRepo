import React, { FC, ReactElement, useEffect, useState } from 'react';
import {
    Paper,
    Typography,
    Box,
    Button,
    Grid,
    Divider,
} from '@mui/material';
import Navbar from '../../Components/Navbar/navbar';
import Footer from '../../Components/Footer/footer';
import { IPage } from '../../shared/models/Page';
import useStyles from '../../Components/Styles/global-styles';
import { useEdit, useGet } from '../../services/api-service';
import { ILesseeDTO } from '../../shared/dtos/LesseeDTO';
import { useNavigate, useParams } from 'react-router-dom';
import useErrorHandling from '../../services/handle-responses';
import { ILessee } from '../../shared/models/Lessee';
import { IApiError } from '../../shared/models/ApiError';

const LesseeFormPage: FC<IPage> = (props): ReactElement => {
    const classes = useStyles('light');
    const { openSnackbar } = props;
    const navigate = useNavigate();
    const { id } = useParams<{ id: string }>();
    const { handleErrorResponse } = useErrorHandling();
    const [fieldErrors, setFieldErrors] = useState<{ [key: string]: string }>({});
    const [editCompleted, setEditCompleted] = useState(false);
    const [editedData, setEditedData] = useState<ILessee | null>(null);

    const { error: editError, editData } = useEdit<ILesseeDTO>(`lessees/${id}`, { id: Number(id) });

    const {
        data: lesseeData,
        error: lesseeError,
    } = useGet<ILessee>(`lessees/${id}`, { id: Number(id) });

    useEffect(() => {
        handleAPIError(lesseeError, openSnackbar, null);
    }, [lesseeError, openSnackbar]);

    useEffect(() => {
        handleAPIError(editError, openSnackbar, null);
    }, [editError, openSnackbar]);

    useEffect(() => {
        handleAPIError(null, openSnackbar, editCompleted);
    }, [editCompleted, openSnackbar]);

    useEffect(() => {
        setEditedData(lesseeData ? { ...lesseeData } : null);
    }, [lesseeData]);

    const handleAPIError = (error: any, snackbar: any, editCompleted: boolean | null) => {
        if (error && [401, 403].includes(error.statusCode)) {
            handleErrorResponse(error.statusCode);
            snackbar(error.message, "error");
        } else if (error?.description.includes("Refresh Token")) {
            navigate("/login");
            openSnackbar("You need to login again", 'warning');
        } else if (error && (error.statusCode === 422)) {
            const fieldErrors = JSON.parse(error.description);
            setFieldErrors(fieldErrors);
        } else if (error) {
            snackbar(error.message, "error");
        } else if (editCompleted) {
            setFieldErrors({});
            navigate(-1);
            openSnackbar('Profile edited successfully', 'success');
        }
    };

    const handleInputChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
        field: string
    ) => {
        setFieldErrors((prevErrors) => ({ ...prevErrors, [field]: '' }));

        setEditedData((prevData) => {
            if (prevData) {
                return { ...prevData, [field]: e.target.value };
            }
            return prevData;
        });
    };

    const handleSaveChanges = async () => {
        if (!editedData) {
            return;
        }

        const lesseeEditedData: ILesseeDTO = {
            name: editedData.name,
            email: editedData.email,
            phoneNumber: editedData.phoneNumber,
        };

        await editData(lesseeEditedData);

        setEditCompleted(true);

        setTimeout(() => setEditCompleted(false), 1000);
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
            <Paper className={classes.paper} elevation={16} square={true}>
                {editError && editError.statusCode !== 422 && (
                    <Box
                        sx={{
                            flexGrow: 1,
                            display: 'flex',
                            width: '100%',
                            justifyContent: 'center',
                            marginTop: 3,
                        }}
                    >
                        <Typography variant="body1" color="red">
                            {editError.description}
                        </Typography>
                    </Box>
                )}
                <Box>
                    <Typography variant="h5" color="black" align='center'>
                        Lessee Profile Edit
                    </Typography>
                    <Divider sx={{ marginBottom: 2 }} />
                </Box>
                <form>
                    <Grid container spacing={2} sx={{ padding: 0.5 }}>
                        {['name', 'email', 'phoneNumber'].map((field) => (
                            <Grid item xs={12} key={field}>
                                <div>
                                    <label style={{ color: 'black', marginRight: "5px" }}>{field.charAt(0).toUpperCase() + field.slice(1)}</label>
                                    <input
                                        type="text"
                                        value={(editedData as any)?.[field] || ''}
                                        onChange={(e) => handleInputChange(e, field as keyof ILessee)}
                                        style={{ width: '30%' }}
                                    />
                                    <span style={{ color: 'red' }}>{fieldErrors[field]}</span>
                                </div>
                            </Grid>
                        ))}
                    </Grid>
                    <Box sx={{ marginTop: 1, display: 'flex', justifyContent: 'flex-end', padding: 1 }}>
                        <Button
                            sx={{
                                color: 'black',
                                backgroundColor: 'white',
                                border: '3px solid #8FBC8F',
                                marginRight: 2,
                            }}
                            onClick={handleSaveChanges}
                        >
                            Save
                        </Button>
                        <Button
                            sx={{
                                color: 'black',
                                backgroundColor: 'white',
                                border: '3px solid #8FBC8F',
                            }}
                            onClick={() => navigate(-1)}
                        >
                            Cancel
                        </Button>
                    </Box>
                </form>
            </Paper>
            <Footer />
        </Box>
    );
};

export default LesseeFormPage;