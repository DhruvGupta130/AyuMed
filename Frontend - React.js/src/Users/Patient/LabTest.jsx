import {useEffect, useState} from 'react';
import axios from 'axios';
import {patientURL} from "../../Api & Services/Api.js";
import {Alert, Button, CircularProgress} from '@mui/material';
import {Download} from '@mui/icons-material';

const LabTest = () => {
    const [testResults, setTestResults] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchLabTests = async () => {
        try {
            const token = localStorage.getItem("token");
            const response = await axios.get(`${patientURL}/lab`, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            setTestResults(response.data);
            console.log(response.data);
        } catch (error) {
            setError("Error fetching Lab Tests. Please try again later.");
            console.error('Error fetching Lab Tests:', error);
        } finally {
            setLoading(false);
        }
    };

    const downloadFile = async (id) => {
        const test = testResults.find(test => test.id === id);
        if (!test?.filePath) return console.error("Test result or file path not found");

        try {
            const response = await fetch(`${test.filePath}`);
            if (!response.ok) throw new Error("Failed to fetch the file");

            const blob = await response.blob();
            const url = URL.createObjectURL(blob);

            const link = document.createElement('a');
            link.href = url;
            link.download = test.filePath.split('/').pop();
            link.click();

            URL.revokeObjectURL(url);
        } catch (error) {
            console.error("Error during file download:", error);
        }
    };

    useEffect(() => {
        fetchLabTests();
    }, []);

    if (loading) {
        return <div className="loader"><CircularProgress/></div>;
    }

    if (error) {
        return (
            <div className='error-box'>
                <Alert sx={{display: "flex", justifyContent: "center", fontSize: "medium"}} severity="error">
                    {error}
                </Alert>
            </div>
        );
    }

    return (
        <div className="labtest-container">
            {testResults.length === 0 && !loading && !error && (
                <div className="info-message">
                    <Alert severity="info" icon={false}>No Lab test available right now.</Alert>
                </div>
            )}

            {/* Display each test result */}
            <div className="test-list">
                {testResults.map((test) => (
                    <div className="test-card" key={test.id}>
                        <h3>{test.testName}</h3>
                        <p><strong>Test Date:</strong> {new Date(test.testDate).toLocaleDateString()}</p>
                        <p><strong>Result:</strong> {test.result || 'No result available'}</p>
                        <p><strong>Notes:</strong> {test.notes || 'No additional notes'}</p>
                        <p><Button color='success' variant='outlined' onClick={() => downloadFile(test.id)}
                                   endIcon={<Download/>}> Download Lab Report</Button></p>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default LabTest;
