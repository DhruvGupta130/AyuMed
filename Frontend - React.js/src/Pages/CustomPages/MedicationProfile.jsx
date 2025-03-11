import axios from "axios";
import {useCallback, useEffect, useState} from "react";
import { useNavigate, useParams } from "react-router-dom";
import { patientURL } from "../../Api & Services/Api";
import { Spin, Typography } from "antd";

const {Text } = Typography;

const MedicationProfile = () => {
    const [medication, setMedication] = useState(null);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const { id } = useParams();
    const navigate = useNavigate();

    const fetchMedication = useCallback(async () => {
        setLoading(true);
        try {
            const token = localStorage.getItem("token");
            const response = await axios.get(`${patientURL}/${id}/medication`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setMedication(response.data);
        } catch (err) {
            setError(err?.response?.data?.message || "Error fetching medication details");
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, [id]);

    useEffect(() => {
        fetchMedication();
    }, [fetchMedication]);

    useEffect(() => {
        if (medication) {
            navigate(`/page/order-medicines?search=${encodeURIComponent(medication.medicationName)}`);
        }
    }, [medication, navigate]);

    return (
        <div style={{ padding: 20, textAlign: "center" }}>
            {loading ? (
                <Spin size="large" style={{ marginTop: 50 }} />
            ) : error ? (
                <Text type="danger" style={{ fontSize: 18 }}>{error}</Text>
            ) : !medication && (
                <Text type="secondary">No medication details found.</Text>
            )}
        </div>
    );
};

export default MedicationProfile;