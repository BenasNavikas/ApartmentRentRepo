import React, { FC, ReactElement, useEffect, useState } from "react";
import { IPage } from "../../shared/models/Page";
import { useNavigate, useParams } from "react-router-dom";
import useStyles from "../../Components/Styles/global-styles";
import useErrorHandling from "../../services/handle-responses";
import { useEdit, useGet } from "../../services/api-service";
import { IRentCase } from "../../shared/models/Rentcases";
import Footer from "../../Components/Footer/footer";
import {
    Box,
    Button,
    Divider,
    Grid,
    Paper,
    TextField,
    Typography,
} from "@mui/material";
import {
    DateTimePicker,
    LocalizationProvider,
} from "@mui/x-date-pickers";
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import dayjs, { Dayjs } from "dayjs";
import { ILesseeDTO } from "../../shared/dtos/LesseeDTO";
import { ILessee } from "../../shared/models/Lessee";
import Navbar from "../../Components/Navbar/navbar";
import { IRentCaseDTO } from "../../shared/dtos/RentCaseDTO";
import { IApiError } from "../../shared/models/ApiError";

const RentCaseFormPage: FC<IPage> = (props): ReactElement => {
    const classes = useStyles("light");
    const { lessorId, rentcaseId } = useParams<{
        lessorId?: string;
        rentcaseId?: string;
    }>();
    const { handleErrorResponse } = useErrorHandling();
    const [fieldErrors, setFieldErrors] = useState<{ [key: string]: string }>({});
    const [editCompleted, setEditCompleted] = useState(false);
    const [editedRentCaseData, setEditedRentCaseData] = useState<IRentCase | null>(null);
    const { openSnackbar } = props;
    const navigate = useNavigate();

    const {
        data: rentCaseData,
        error: rentCaseError,
    } = useGet<IRentCase>('rentcases/{id}', { id: Number(rentcaseId) });

    const { data: editRentCaseData, error: editRentCaseError, editData: editDataRentCase } = useEdit<IRentCaseDTO>('rentcases/{rentcaseId}', { lessorId: Number(lessorId), rentcaseId: Number(rentcaseId) });

    useEffect(() => handleAPIError(rentCaseError, openSnackbar, null), [rentCaseError, openSnackbar]);
    useEffect(() => handleAPIError(editRentCaseError, openSnackbar, editCompleted), [editCompleted, editRentCaseError, openSnackbar]);
    useEffect(() => { setEditedRentCaseData(rentCaseData ? { ...rentCaseData } : null); }, [rentCaseData]);

    useEffect(() => {
        if (editRentCaseData) {
            setEditCompleted(true);
            setFieldErrors({});
            const timeoutId = setTimeout(() => setEditCompleted(false), 1000);
            return () => clearTimeout(timeoutId);
        }
    }, [editRentCaseData]);

    const handleAPIError = (error: any, snackbar: any, editCompleted: boolean | null) => {
        if (error && [401, 403].includes(error.statusCode)) {
            handleErrorResponse(error.statusCode);
            snackbar(error.message, "error");
        } else if (error?.description.includes("Refresh Token")) {
            navigate("/login");
            openSnackbar("You need to login again", 'warning');
        } else if (error && (error.statusCode === 422) && Object.keys(fieldErrors).length > 0) {
            const fieldErrors = JSON.parse(error.description);
            setFieldErrors((prevErrors) => ({ ...prevErrors, ...fieldErrors }));
        } else if (error) {
            snackbar(error.message, "error");
        } else if (Object.keys(fieldErrors).length === 0 && editCompleted) {
            setFieldErrors({});
            navigate("/rentcases");
            openSnackbar('Rent Case edited successfully', 'success');
        }
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>, field: keyof IRentCase) => {
        setFieldErrors((prevErrors) => ({ ...prevErrors, [field]: '' }));
        setEditedRentCaseData((prevData) => {
            if (prevData) {
                return {
                    ...prevData,
                    [field]: e.target.value, // Update the field with the new value
                };
            }
            return prevData;
        });
    };
    

    const handleDateTimeChange = (date: Dayjs | null, field: keyof IRentCase) => {
        setFieldErrors((prevErrors) => ({ ...prevErrors, [field]: '' }));
        setEditedRentCaseData((prevData) => {
            if (prevData) {
                const dateToSet = date ? dayjs(date).format('YYYY-MM-DD HH:mm:ss') : null;
                const formattedDate = dateToSet?.replace(/[ZT]/g, '');
                return { ...prevData, [field]: formattedDate };
            }
            return prevData;
        });
    };
    

    const handleSaveChanges = async () => {
        if (!editedRentCaseData) {
            return;
        }

        const rentCaseRequest: IRentCaseDTO = {
            rentAmount: editedRentCaseData.rentAmount,
            dueDate: editedRentCaseData.dueDate
        };

        await editDataRentCase(rentCaseRequest);
    };

    return (
        <Box className={classes.body} sx={{
            flexGrow: 1,
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'space-between',
            minHeight: '100vh',
            height: '100%',
            overflow: 'hidden',
            backgroundColor: '#8FBC8F',
        }}>
            <Navbar title="RentEase" />
            <Paper className={classes.paper} elevation={16} square={true}>
                {editRentCaseError !== null && editRentCaseError.statusCode !== 422 && (
                    <Box sx={{
                        flexGrow: 1,
                        display: 'flex',
                        width: '100%',
                        justifyContent: 'center',
                        height: '200px',
                        marginTop: 3,
                    }}>
                        <Typography variant="body1" color="red">
                            {editRentCaseError.description}
                        </Typography>
                    </Box>
                )}
                <Box>
                    <Typography variant="h5" color="black" align="center">
                        Rent Case Edit
                    </Typography>
                    <Divider sx={{ marginBottom: 2 }} />
                </Box>
                <form>
                    <Grid container spacing={2} sx={{ padding: 0.5 }}>
                        {['rentAmount', 'dueDate'].map((field) => (
                            <Grid item xs={12} key={field}>
                                <div>
                                    <label style={{ color: 'black', marginRight: '5px' }}>
                                        {field
                                            .split(/(?=[A-Z])/)
                                            .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
                                            .join(' ')}
                                    </label>
                                    {field === 'dueDate' ? (
                                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                                            <DateTimePicker
                                                value={dayjs((editedRentCaseData as any)?.[field])}
                                                views={['year', 'day', 'hours', 'minutes', 'seconds']}
                                                onChange={(date) => handleDateTimeChange(date, field as keyof IRentCase)}
                                            />
                                        </LocalizationProvider>
                                    ) : (
                                        <input
                                            type="text"
                                            value={(editedRentCaseData as any)?.[field] || ''}
                                            onChange={(e) => handleInputChange(e, field as keyof IRentCase)}
                                            style={{ width: '30%' }}
                                        />
                                    )}
                                    <span style={{ color: 'red' }}>{fieldErrors[field]}</span>
                                </div>
                            </Grid>
                        ))}
                    </Grid>
                    <Box sx={{ marginTop: 1, display: 'flex', justifyContent: 'flex-end', padding: 1 }}>
                        <Button sx={{
                            color: 'black',
                            backgroundColor: 'white',
                            border: '3px solid #8FBC8F',
                            marginRight: 2,
                        }} onClick={handleSaveChanges}>
                            Save
                        </Button>
                        <Button sx={{
                            color: 'black',
                            backgroundColor: 'white',
                            border: '3px solid #8FBC8F',
                        }} onClick={() => navigate("/rentcases")}>
                            Cancel
                        </Button>
                    </Box>
                </form>
            </Paper>
            <Footer />
        </Box>
    );
};

export default RentCaseFormPage;
