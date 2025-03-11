import { useCallback, useEffect, useState } from "react";
import { Button, LinearProgress, Dialog, DialogContent, DialogTitle, CircularProgress, Alert} from "@mui/material";
import { Delete, Download, Preview, UploadTwoTone } from "@mui/icons-material";
import { patientURL } from "../../Api & Services/Api.js";
import { format } from "date-fns";
import PropTypes from "prop-types";
import { Worker, Viewer } from "@react-pdf-viewer/core";
import "@react-pdf-viewer/core/lib/styles/index.css";
import axios from "axios";

const DocumentViewer = ({ onUploadClick }) => {
  const [documents, setDocuments] = useState([]);
  const [loading, setLoading] = useState(true);
  const token = localStorage.getItem("token");
  const [error, setError] = useState("");
  const [actionError, setActionError] = useState('');
  const [message, setMessage] = useState('');
  const [previewFile, setPreviewFile] = useState(null);
  const [isPreviewOpen, setIsPreviewOpen] = useState(false);

  const getDocument = async (id, isPreview = false) => {
    try {
      const response = await axios.get(`${patientURL}/${id}/download`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        responseType: 'blob',
      });
  
      if (response.status !== 200) {
        setActionError("Failed to get document");
        return;
      }
  
      const blob = response.data;
      const fileURL = window.URL.createObjectURL(blob);
  
      if (isPreview) {
        setPreviewFile({ id, fileURL, fileType: blob.type });
        setIsPreviewOpen(true);
      } else {
        const link = document.createElement("a");
        link.href = fileURL;
        link.setAttribute("download", `document_${id}`);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      }
    } catch (error) {
      setError("Error getting file!");
      setTimeout(() => setError(""), 3000);
      console.error("Error getting file:", error);
    } finally {
      setLoading(false);
    }
  };  

  const handleDelete = async (id) => {
    const confirmDelete = window.confirm("Are you sure you want to delete this document?");
    if (!confirmDelete) {
      return;
    }
    try {
      const response = await axios.delete(`${patientURL}/${id}/delete`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log(response);
      setMessage(response.data?.message);
      setTimeout(() => setMessage(""), 2000);
      setTimeout( async () => await fetchDocuments(), 2100);
    } catch (error) {
      if (error.response?.data?.message) {
        setActionError(error.response.data.message || "Error deleting file!");
      } else {
        setActionError("An error occurred. Please try again.");
      }
      setTimeout(() => setActionError(""), 3000);
      console.error("Error deleting file: ", error);
    } finally {
      setLoading(false);
    }
    
  };

  const fetchDocuments = useCallback(async () => {
    try {
      const response = await fetch(`${patientURL}/files`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (!response.ok) setError("Failed to fetch documents");
      const data = await response.json();
      setDocuments(data);
    } catch (err) {
      console.error(err);
      setError("Unable to load documents. Please try again.");
    } finally {
      setLoading(false);
    }
  }, [token]);

  useEffect(() => {
    fetchDocuments().then(r => r);
  }, [fetchDocuments]);

  if (loading) {
      return <div className="loader"><CircularProgress/></div>;
    }

  if (error) {
    return (
    <Alert sx={{ display: "flex", justifyContent: "center", fontSize: "medium", width: '100%' }} severity="error">
      {error}
    </Alert>);
  }

  const formatDate = (date) => {
    const d = format(new Date(date), "MMMM dd, yyyy");
    const t = format(new Date(date), "hh:mm a");
    return `${d}, ${t}`;
  };

  return (
    <div className="view-documents-box">
      <h2 className="view-heading">Your Documents</h2>
      <div className="upload-info">
        {loading && <LinearProgress sx={{ height: 8, marginY: 1 }} />}
        {!loading && !error && documents.length === 0 && <span>No documents found.</span>}
        {actionError && 
          <Alert sx={{ display: "flex", justifyContent: "center", fontSize: "medium" }} severity="error">
            {actionError}
          </Alert>}
        {message && (
          <Alert sx={{ display: "flex", justifyContent: "center", fontSize: "medium" }} severity="success">
            {message}
          </Alert>
        )}
      </div>
      {!loading && !error && documents.length !== 0 && (
        <div className="table-container">
          <table className="table">
            <thead>
              <tr className="documents-details-headings">
                <th className="documents-headings">Document Name</th>
                <th className="documents-headings">Description</th>
                <th className="documents-headings">File Type</th>
                <th className="documents-headings">Uploaded Date</th>
                <th className="documents-headings">Actions</th>
              </tr>
            </thead>
            <tbody>
              {documents?.map((doc) => (
                <tr key={doc.id} className="table-row">
                  <td className="documents-details">{doc?.fileName}</td>
                  <td className="documents-details">{doc?.description}</td>
                  <td className="documents-details">{doc?.fileType}</td>
                  <td className="documents-details">{formatDate(doc?.uploadDate)}</td>
                  <td className="documents-details">
                    <Button
                      variant="outlined"
                      color="info"
                      sx={{ margin: 0.3, width: "100%" }}
                      onClick={() => getDocument(doc.id, true)}
                      className="preview-document"
                    >
                      <Preview /> Preview
                    </Button>
                    <Button
                      variant="outlined"
                      color="success"
                      sx={{ margin: 0.3, width: "100%" }}
                      onClick={() => getDocument(doc.id)}
                      className="view-document"
                    >
                      <Download /> Download
                    </Button>
                    <Button
                      variant="outlined"
                      sx={{ margin: 0.3, width: "100%" }}
                      color="error"
                      onClick={() => handleDelete(doc.id)}
                      className="delete-document"
                    >
                      <Delete /> Delete
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {!loading && !error && (
        <Button
          variant="contained"
          className="upload-btn"
          sx={{ margin: 2 }}
          onClick={onUploadClick}
        >
          <UploadTwoTone sx={{ marginRight: 1 }} />
          Upload Documents
        </Button>
      )}

      {/* Preview Modal */}
      <Dialog open={isPreviewOpen} onClose={() => setIsPreviewOpen(false)} fullWidth maxWidth="md">
        <DialogTitle>Preview Document</DialogTitle>
        <DialogContent>
          {previewFile?.fileType === "application/pdf" ? (
            <Worker workerUrl="https://unpkg.com/pdfjs-dist@3.11.174/build/pdf.worker.min.js">
              <Viewer fileUrl={previewFile.fileURL} />
            </Worker>
          ) : previewFile?.fileType.startsWith("image/") ? (
            <img src={previewFile.fileURL} alt="Preview" style={{ maxWidth: "100%" }} />
          ) : (
            <p>Preview is not available for this file type.</p>
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
};

DocumentViewer.propTypes = {
  onUploadClick: PropTypes.func.isRequired, // onUploadClick should be a function and is required
};

export default DocumentViewer;
