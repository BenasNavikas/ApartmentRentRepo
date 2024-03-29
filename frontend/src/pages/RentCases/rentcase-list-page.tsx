import React, { FC, ReactElement, useEffect, useState } from "react";
import {
  Box,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Button,
  Typography,
  Grid,
  Dialog,
  DialogTitle, 
  DialogContent,
  DialogActions,
} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import useStyles from "../../Components/Styles/global-styles";
import { useNavigate } from "react-router-dom";
import Navbar from "../../Components/Navbar/navbar";
import Footer from "../../Components/Footer/footer";
import { IPage } from "../../shared/models/Page";
import { IRentCase } from "../../shared/models/Rentcases";
import { useDelete, useGet, usePost } from "../../services/api-service";
import useErrorHandling from "../../services/handle-responses";
import { ILessor } from "../../shared/models/Lessor";

const RentcaseListPage: FC<IPage> = (props): ReactElement => {
  const classes = useStyles("light");
  const navigate = useNavigate();
  const role = localStorage.getItem("role");
  const username = localStorage.getItem("username");
  const [shouldRefetch, setShouldRefetch] = useState(false);
  const [confirmationDialogOpen, setConfirmationDialogOpen] = useState(false);
  const [rentCaseToDelete, setRentCaseToDelete] = useState<{ lessorId: number, id: number } | null>(null);
  const [lessorId, setLessorId] = useState<number | undefined>(undefined);
  const { handleErrorResponse } = useErrorHandling();
  const { openSnackbar } = props;

  const roleSpecificEndpoint = (() => {
    switch (role) {
      case "LESSOR":
        return `rentcases/lessor/${username}`;
      case "LESSEE":
        return `rentcases/lessee/${username}`;
      case "ADMIN":
        return "rentcases";
      default:
        return "";
    }
  })();

  const { data: deletedData, error: deleteError, deleteData } = useDelete<any>("rentcases/{id}", { lessorId: rentCaseToDelete?.lessorId, id: rentCaseToDelete?.id });
  const { data: fileData, error: fileError, postData } = usePost<FormData>("lessor/{id}/rentcases/file", { id: lessorId });
  const { data: rentCaseData, loading: rentCaseLoading, error } = useGet<IRentCase[]>(
    roleSpecificEndpoint,
    role === "ADMIN" ? {} : username ? { username } : {},
    shouldRefetch
  );
  console.log(rentCaseData?.map(rentCase => rentCase.rentCaseId));
  console.log(rentCaseData);

  useEffect(() => {
    if (error && [401, 403].includes(error.statusCode)) {
      handleErrorResponse(error.statusCode);
      openSnackbar(error.message, 'error');
    } else if (error?.description.includes("Refresh Token")) {
      navigate("/login");
      openSnackbar("You need to login again", 'warning');
  } 
  }, [error, openSnackbar]);

  useEffect(() => {
    if (!rentCaseLoading) {
      setShouldRefetch(false);
    }
  }, [rentCaseLoading]);

  useEffect(() => {
    if (rentCaseData && lessorId === undefined && role === "LESSOR") {
      const userWithSameUsername = rentCaseData.find(rentCase => rentCase.lessor.user.username === username);

      if (userWithSameUsername) {
        setLessorId(userWithSameUsername.lessor.id);
      }
    }
  }, [rentCaseData]);

  const handleEdit = (lessorId: number, id: number) => {
    navigate(`/rentcases/${id}`);
  };

  const handleDelete = (lessorId: number, id: number) => {
    setRentCaseToDelete({ lessorId, id });
    setConfirmationDialogOpen(true);
  };

  const handleFileUpload = (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    const file = event.target.files?.[0];

    if (file) {
      const formData = new FormData();
      formData.append("file", file);
      postData(formData, true);
    }
  };

  useEffect(() => {
    if (fileData) {
      openSnackbar("File uploaded successfully", 'success');
      setShouldRefetch(true);
      setLessorId(undefined);
    } else if (fileError) {
      openSnackbar(fileError.description, 'error');
    }
  }, [fileData, fileError, openSnackbar]);

  const handleDeleteConfirmed = async () => {
    deleteData();
  };

  const handleDeleteCancelled = () => {
    setConfirmationDialogOpen(false);
    setRentCaseToDelete(null);
  };

  useEffect(() => {
    if (deletedData && deletedData.statusCode === 204 && deletedData.description.includes("Deleted successfully")) {
      openSnackbar("Rent Case deleted successfully", 'success');
      setShouldRefetch(true);
      setConfirmationDialogOpen(false);
    } else if (deleteError && deleteError.statusCode !== 204) {
      openSnackbar(deleteError.description, 'error');
    }

    setRentCaseToDelete(null);
  }, [deleteError, deletedData, openSnackbar]);

  const renderActionButtons = (lessorId: number, id: number) => (
    <Grid container spacing={1} justifyContent="flex-end">
      {["Edit", role === "ADMIN" && "Delete"].filter(Boolean).map((action) => (
        <Grid item key={`${action}-${id}`}>
          <Button
            sx={{
              color: "black",
              backgroundColor: "white",
              border: "3px solid #8FBC8F",
            }}
            onClick={() => (action === "Edit" ? handleEdit(lessorId, id) : handleDelete(lessorId, id))}
          >
            {action}
          </Button>
        </Grid>
      ))}
    </Grid>
  );

  const groupedRentCases = rentCaseData?.reduce((acc, rentCase) => {
    const status = rentCase.rentCaseStatus.status.toLowerCase();
    acc[status] = acc[status] || [];
    acc[status].push(rentCase);
    return acc;
  }, {} as Record<string, IRentCase[]>);
  console.log(groupedRentCases);
  const renderAccordionSection = (title: string, cases: IRentCase[], status: string) => (
    <div>
      <Typography sx={{ marginTop: "10px" }} variant="h6" gutterBottom>
        {title}
      </Typography>
      {cases.map((rentCase: IRentCase, index: number) => (
        <Accordion
          key={`${status}-${index}`}
          sx={{
            background: "white",
            boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.1)",
            "&:hover": {
              boxShadow: "0px 8px 16px rgba(0, 0, 0, 0.2)",
            },
          }}
        >
          <AccordionSummary
            expandIcon={<ExpandMoreIcon sx={{ color: "#2E8B57" }} />}
            sx={{ borderBottom: "1px solid #ccc" }}
          >
            <Typography style={{ marginRight: "5px" }}>Lessor name: {rentCase.lessor?.name}</Typography>
          </AccordionSummary>
          <AccordionDetails>
            <Typography>Lessee name: {rentCase.lessee.name}</Typography>
            <Typography>Due Date: {rentCase.dueDate}</Typography>
            <Typography>Rent Amount: {rentCase.rentAmount}€</Typography>
            <Typography>Is Notification Sent? {rentCase.isSent ? "No" : "Yes"}</Typography>
            {(role === "LESSOR" || role === "ADMIN")
              && renderLesseeDetails(rentCase.lessee)
              && renderActionButtons(rentCase.lessor.id, rentCase.rentCaseId)}
          </AccordionDetails>
        </Accordion>
      ))}
    </div>
  );

  const renderLesseeDetails = (lessee: any) => (
    <React.Fragment>
      <Typography variant="subtitle1">Lessee: {lessee.name} {lessee.surname}</Typography>
      <Typography variant="body2">Email: {lessee.email}</Typography>
      <Typography variant="body2">Phone Number: {lessee.phoneNumber}</Typography>
    </React.Fragment>
  );

  return (
    <>
      <Box className={classes.body} sx={{ flexGrow: 1, display: "flex", flexDirection: "column", justifyContent: "space-between", minHeight: "100vh", height: "100%", overflow: "hidden" }}>
        <Navbar title="RentEase" />
        <Box sx={{ padding: "16px" }}>
        <Grid container spacing={1} justifyContent="flex-start">
  <Grid item>
    <Button
      sx={{
        color: "black",
        backgroundColor: "white",
        border: "2px solid",
        marginRight: "10px",
        "&:hover": {
          color: "black",
          backgroundColor: "#F8DE7E",
        },
      }}
      onClick={() => navigate(-1)}
    >
      Back
    </Button>
  </Grid>
  {role === "ADMIN" && (
    <Grid item>
      <label htmlFor="file-upload">
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
            },
          }}
          onClick={() => navigate("/users/newrentcase")}
        >
          Create RentCase
        </Button>
      </label>
      <input
        id="file-upload"
        type="file"
        style={{ display: "none" }}
        onChange={(event) => handleFileUpload(event)}
      />
    </Grid>
  )}
</Grid>

          {rentCaseLoading ? (
            <Typography>Loading...</Typography>
          ) : (
            <Box>
              {groupedRentCases && Object.entries(groupedRentCases).map(([status, cases]) => (
                renderAccordionSection(status === 'unpaid' ? 'Unpaid Rent Cases' : status.charAt(0).toUpperCase() + status.slice(1) + ' Rent Cases', cases, status)
              ))}
            </Box>
          )}
          {rentCaseToDelete && (
            <Dialog open={confirmationDialogOpen} onClose={handleDeleteCancelled}>
              <DialogTitle>Confirm Deletion</DialogTitle>
              <DialogContent>
                <Typography>
                  Are you sure you want to delete this rent case?
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

export default RentcaseListPage;