import {useCallback, useEffect, useState} from "react";
import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField, Rating } from "@mui/material";
import axios from "axios";
import PropTypes from "prop-types";
import { patientURL } from "../../Api & Services/Api";

const FeedbackModal = ({ feedbackModal, closeFeedbackModal, appointmentId }) => {
  const [feedback, setFeedback] = useState({
    rating: 0,
    comments: '',
  });
  const [loading, setLoading] = useState(false);

  const handleFetchFeedback = useCallback(async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(`${patientURL}/feedback/${appointmentId}`,{
        headers:{
            Authorization: `Bearer ${token}`
          }
        });
      setFeedback(response.data);
    } catch (error) {
      console.error("Error fetching feedback:", error);
    } finally {
      setLoading(false);
    }
  }, [appointmentId]);

  useEffect(() => {
    if(appointmentId && feedbackModal){
        handleFetchFeedback();
    }
  }, [appointmentId, feedbackModal, handleFetchFeedback]);

  const handleSubmitFeedback = async () => {
    if (feedback.rating === 0 || feedback.comments.trim() === '') {
      alert("Please provide a rating and comment.");
      return;
    }
    setLoading(true);
    try {
        const token = localStorage.getItem('token');
        const response = await axios.post(`${patientURL}/feedback/${appointmentId}`, feedback, {
          headers:{
              Authorization: `Bearer ${token}`
            }
        });
      alert(response.data.message);
      closeFeedbackModal();
    } catch (error) {
      console.error("Error submitting feedback:", error);
      alert(error?.response?.data?.message || "Failed to submit feedback. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Dialog open={feedbackModal} onClose={closeFeedbackModal}>
      <DialogTitle>Submit Feedback</DialogTitle>
      <DialogContent>
        <Rating 
          value={feedback.rating} 
          onChange={(event, newValue) => setFeedback(prev => ({ ...prev, rating: newValue }))} 
          precision={1} 
        />
        <TextField
          fullWidth
          multiline
          minRows={3}
          label="Comments"
          value={feedback.comments}
          onChange={(e) => {
            if (e.target.value.length <= 500) {
              setFeedback(prev => ({ ...prev, comments: e.target.value }));
            }
          }}
          helperText={`${feedback.comments.length}/500`}
          margin="dense"
        />

      </DialogContent>
      <DialogActions>
        <Button onClick={closeFeedbackModal} color="primary">Cancel</Button>
        <Button onClick={handleSubmitFeedback} disabled={loading} color="error">
          {loading ? "Submitting..." : "Confirm"}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

FeedbackModal.propTypes = {
  feedbackModal: PropTypes.bool.isRequired,
  closeFeedbackModal: PropTypes.func.isRequired,
  appointmentId: PropTypes.number.isRequired
};

export default FeedbackModal;
