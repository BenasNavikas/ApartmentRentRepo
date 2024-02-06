import React, { FC, ReactElement, useEffect, useState } from "react";
import {
    Box,
    Button,
    Typography,
    Grid,
    Accordion,
    AccordionSummary,
    AccordionDetails,
    TextField,
    Dialog,
    DialogTitle,
    DialogActions,
    DialogContent,
} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import useStyles from "../../Components/Styles/global-styles";
import { useNavigate } from "react-router-dom";
import Navbar from "../../Components/Navbar/navbar";
import Footer from "../../Components/Footer/footer";
import { IPage } from "../../shared/models/Page";
import { useDelete, useGet } from "../../services/api-service";
import useErrorHandling from "../../services/handle-responses";
import { ILessor } from "../../shared/models/Lessor";
import { ILessee } from "../../shared/models/Lessee";
import { IApiError } from "../../shared/models/ApiError";

const UserListPage: FC<IPage> = (props): ReactElement => {
    const navigate = useNavigate();
    const classes = useStyles('light');
    const [shouldRefetch, setShouldRefetch] = useState(false);
    const [confirmationDialogOpen, setConfirmationDialogOpen] = useState(false);
    const [createLesseeDialogOpen, setCreateLesseeDialogOpen] = useState(false);
    const [userToDelete, setUserToDelete] = useState<{ id: number, type: string } | null>(null);
    const [showMoreLessor, setShowMoreLessor] = useState(3);
    const [showMoreLessee, setShowMoreLessee] = useState(3);
    const [lessorSearchQuery, setLessorSearchQuery] = useState("");
    const [lesseeSearchQuery, setLesseeSearchQuery] = useState("");
    const { handleErrorResponse } = useErrorHandling();
    const { openSnackbar } = props;

    const {
        data: lessorData,
        loading: lessorLoading,
        error: lessorError,
    } = useGet<ILessor[]>("lessors", {}, shouldRefetch);

    const {
        data: lesseeData,
        loading: lesseeLoading,
        error: lesseeError,
    } = useGet<ILessee[]>("lessees", {}, shouldRefetch);

    const useDeleteLessor = (id: number) => {
        const { data, loading, error, deleteData } = useDelete<any>("lessors/{id}", { id: id });
        return { data, loading, error, deleteLessor: deleteData };
    };

    const useDeleteLessee = (id: number) => {
        const { data, loading, error, deleteData } = useDelete<any>("lessees/{id}", { id: id });
        return { data, loading, error, deleteLessee: deleteData };
    };

    const { data: deletedDataLessor, error: deleteErrorLessor, deleteLessor } = useDeleteLessor(userToDelete?.id || 0);
    const { data: deletedDataLessee, error: deleteErrorLessee, deleteLessee } = useDeleteLessee(userToDelete?.id || 0);

    const filteredLessors = lessorData
        ? lessorData.filter(
            (lessor: ILessor) =>
                lessor.user?.username.includes(lessorSearchQuery) ||
                lessor.name.includes(lessorSearchQuery) //||
                // lessor.email.includes(lessorSearchQuery) ||
                // lessor.phoneNumber.includes(lessorSearchQuery)
        )
        : [];

    const filteredLessees = lesseeData
        ? lesseeData.filter(
            (lessee: ILessee) =>
                lessee.user?.username.includes(lesseeSearchQuery) ||
                lessee.name.includes(lesseeSearchQuery) ||
                lessee.email?.includes(lesseeSearchQuery) ||
                lessee.phoneNumber?.includes(lesseeSearchQuery)
        )
        : [];

    useEffect(() => {
        handleAPIError(lessorError, openSnackbar);
    }, [lessorError, openSnackbar]);

    useEffect(() => {
        handleAPIError(lesseeError, openSnackbar);
    }, [lesseeError, openSnackbar]);

    useEffect(() => {
        handleAPIError(deleteErrorLessor, openSnackbar);
    }, [deleteErrorLessor, openSnackbar]);

    useEffect(() => {
        handleAPIError(deleteErrorLessee, openSnackbar);
    }, [deleteErrorLessee, openSnackbar]);

    useEffect(() => {
        handleAPIError(deletedDataLessor, openSnackbar);
    }, [deletedDataLessor, openSnackbar]);

    useEffect(() => {
        handleAPIError(deletedDataLessee, openSnackbar);
    }, [deletedDataLessee, openSnackbar]);

    const handleAPIError = (error: any, snackbar: any) => {
        if (error && [401, 403].includes(error.statusCode)) {
            handleErrorResponse(error.statusCode);
            snackbar(error.message, "error");
        } else if (error && error.statusCode === 204) {
            setShouldRefetch(true);
            setConfirmationDialogOpen(false);
            openSnackbar("User deleted successfully", 'success');
        } else if (error) {
            setConfirmationDialogOpen(false);
            snackbar(error.message, "error");
        } else if (error?.description.includes("Refresh Token")) {
            navigate("/login");
            openSnackbar("You need to login again", 'warning');
        } 
    };

    const handleEdit = (id: number, type: string) => {
        if (type === 'lessor') {
            navigate(`/lessors/${id}`);
        } else if (type === 'lessee') {
            navigate(`/lessees/${id}`);
        }
    };

    const handleDelete = (id: number, type: string) => {
        setUserToDelete({ id, type });
        setConfirmationDialogOpen(true);
    };

    const handleDeleteConfirmed = async () => {
        if (userToDelete) {
            if (userToDelete.type === 'lessor') {
                await deleteLessor();
            } else if (userToDelete.type === 'lessee') {
                await deleteLessee();
            }
        }
    }

    const handleDeleteCancelled = () => {
        setConfirmationDialogOpen(false);
        setUserToDelete(null);
    };

    const renderActionButtons = (id: number, type: string) => (
        <Grid container spacing={1} justifyContent="flex-end">
            {["Edit", "Delete"].map((action) => (
                <Grid item key={`${action}-${id}`}>
                    <Button
                        sx={{
                            color: "black",
                            backgroundColor: "white",
                            border: "3px solid #8FBC8F",
                        }}
                        onClick={() =>
                            action === "Edit" ? handleEdit(id, type) : handleDelete(id, type)
                        }
                    >
                        {action}
                    </Button>
                </Grid>
            ))}
        </Grid>
    );

    const renderExpandButton = (
        showMore: number,
        setShowMore: React.Dispatch<React.SetStateAction<number>>,
        data: ILessor[] | ILessee[],
        label: string
    ) => (
        <Button
            sx={{
                color: "black",
                backgroundColor: "white",
                border: "2px solid",
                marginBottom: "5px",
                "&:hover": {
                    color: "black",
                    backgroundColor: "#F8DE7E",
                },
            }}
            onClick={() => setShowMore(showMore + 5)}
            disabled={showMore >= data.length}
        >
            Show More {label}
        </Button>
    );

    return (
        <>
            <Box
                className={classes.body}
                sx={{
                    flexGrow: 1,
                    display: "flex",
                    flexDirection: "column",
                    justifyContent: "space-between",
                    minHeight: "100vh",
                    height: "100%",
                    overflow: "hidden",
                }}
            >
                <Navbar title="RentEase" />
                <Box sx={{ padding: "16px" }}>
                    <Button
                        sx={{
                            color: "black",
                            backgroundColor: "white",
                            border: "2px solid",
                            "&:hover": {
                                color: "black",
                                backgroundColor: "#F8DE7E",
                            },
                        }}
                        onClick={() => navigate(-1)}
                    >
                        Back
                    </Button>
                    {lessorLoading || lesseeLoading ? (
                        <Typography>Loading...</Typography>
                    ) : (
                        <Box>
                            <Typography variant="h5" sx={{ display: 'flex', alignItems: 'center' }}>
                                Lessors
                                <Box sx={{ marginLeft: 'auto', display: 'flex', alignItems: 'center' }}>
                                    <TextField
                                        label="Search Lessors"
                                        sx={{ background: "white" }}
                                        variant="outlined"
                                        value={lessorSearchQuery}
                                        onChange={(e) => setLessorSearchQuery(e.target.value)}
                                        margin="normal"
                                        size="small"
                                    />
                                </Box>
                            </Typography>
                            {renderExpandButton(showMoreLessor, setShowMoreLessor, lessorData || [], "Lessors")}
                            <Button
                                sx={{
                                    color: "black",
                                    backgroundColor: "white",
                                    border: "2px solid",
                                    marginBottom: "5px",
                                    marginLeft: "5px",
                                    "&:hover": {
                                        color: "black",
                                        backgroundColor: "#F8DE7E",
                                    }
                                }}
                                onClick={() => navigate("/users/newlessor")}
                            >
                                Create Lessor
                            </Button>
                            {filteredLessors?.slice(0, showMoreLessor).map((lessor) => (
                                <Accordion key={lessor.id}>
                                    <AccordionSummary
                                        expandIcon={<ExpandMoreIcon />}
                                        aria-controls={`lessor-details-${lessor.id}`}
                                        id={`lessor-details-${lessor.id}`}
                                    >
                                        <Typography>{lessor.name}</Typography>
                                    </AccordionSummary>
                                    <AccordionDetails>
                                        <Box>
                                            <Typography>{`Address: ${lessor.address}`}</Typography>
                                            <Typography>{`Phone Number: ${lessor.phoneNumber}`}</Typography>
                                            <Typography>{`Email: ${lessor.email}`}</Typography>
                                            <Typography>{`Bank Account Number: ${lessor.accountNumber}`}</Typography>
                                            <Typography>{`Username: ${lessor.user?.username}`}</Typography>
                                            {renderActionButtons(lessor.id, "lessor")}
                                        </Box>
                                    </AccordionDetails>
                                </Accordion>
                            ))}
                            <Typography variant="h5" sx={{ display: 'flex', alignItems: 'center' }}>
                                Lessees
                                <Box sx={{ marginLeft: 'auto', display: 'flex', alignItems: 'center' }}>
                                    <TextField
                                        label="Search Lessees"
                                        sx={{ background: "white" }}
                                        variant="outlined"
                                        value={lesseeSearchQuery}
                                        onChange={(e) => setLesseeSearchQuery(e.target.value)}
                                        margin="normal"
                                        size="small"
                                    />
                                </Box>
                            </Typography>
                            {renderExpandButton(showMoreLessee, setShowMoreLessee, lesseeData || [], "Lessees")}
                            <Button
                                sx={{
                                    color: "black",
                                    backgroundColor: "white",
                                    border: "2px solid",
                                    marginBottom: "5px",
                                    marginLeft: "5px",
                                    "&:hover": {
                                        color: "black",
                                        backgroundColor: "#F8DE7E",
                                    }
                                }}
                                onClick={() => navigate("/users/newlessee")}
                            >
                                Create Lessee
                            </Button>
                            {filteredLessees?.slice(0, showMoreLessee).map((lessee) => (
                                <Accordion key={lessee.id}>
                                    <AccordionSummary
                                        expandIcon={<ExpandMoreIcon />}
                                        aria-controls={`lessee-details-${lessee.id}`}
                                        id={`lessee-details-${lessee.id}`}
                                    >
                                        <Typography>{`${lessee.name}`}</Typography>
                                    </AccordionSummary>
                                    <AccordionDetails>
                                        <Box>
                                            <Typography>{`Email: ${lessee.email}`}</Typography>
                                            <Typography>{`Phone Number: ${lessee.phoneNumber}`}</Typography>
                                            <Typography>{`Username: ${lessee.user?.username}`}</Typography>
                                            {renderActionButtons(lessee.id, "lessee")}
                                        </Box>
                                    </AccordionDetails>
                                </Accordion>
                            ))}
                        </Box>
                    )}
                    {userToDelete && (
                        <Dialog open={confirmationDialogOpen} onClose={handleDeleteCancelled}>
                            <DialogTitle>Confirm Deletion</DialogTitle>
                            <DialogContent>
                                <Typography>
                                    Are you sure you want to delete this user?
                                </Typography>
                            </DialogContent>
                            <DialogActions>
                                <Button sx={{
                                    color: "black",
                                    backgroundColor: "white",
                                    border: "3px solid #8FBC8F",
                                    marginRight: 2,
                                }}
                                    onClick={handleDeleteCancelled}>Cancel</Button>
                                <Button
                                    sx={{
                                        color: "red",
                                        backgroundColor: "white",
                                        border: "3px solid #8FBC8F",
                                        marginRight: 2,
                                    }}
                                    onClick={handleDeleteConfirmed}>
                                    Confirm
                                </Button>
                            </DialogActions>
                        </Dialog>
                    )}
                </Box>
                <Footer />
            </Box>
        </>
    );
};

export default UserListPage;