import { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import { Button, Table, Card, Spin, Alert } from 'antd';
import { patientURL } from '../../Api & Services/Api';
import Title from 'antd/es/typography/Title';
import { MedicationSharp } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

const Medications = () => {
  const [Medications, setMedications] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const fetchMedications = useCallback(async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(`${patientURL}/medications`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setMedications(response.data);
      console.log(response.data);
    } catch (error) {
      if(error.response.status !==500){
        setError(error?.response?.data?.message);
      } else{
        setError("Error while fetching Medications. Please try again!");
      }
      console.error("Error in fetching Medications: ", error);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchMedications();
  }, [fetchMedications]);

  return (
    <div className="Medications-details" style={{ padding: '24px' }}>
        <Card title={<Title level={3}>Medications List</Title>} className="Medications-list-card">
        {loading ? (
            <Spin
            tip="Loading Medications..."
            size="large"
            style={{ display: "block", margin: "auto" }}
            >
            <div style={{height: '200px'}}/>
            </Spin>
        ) : error ? (
            <Alert message={error} type="error" showIcon/>
        ) : (
            <Table
            dataSource={Medications}
            rowKey="id"
            columns={[
            {
                title: '',
                dataIndex: 'isExpired',
                key: 'status',
                render: (isExpired) => {
                return isExpired ? (
                    <div
                    style={{
                        height: '10px',
                        width: '10px',
                        backgroundColor: 'red',
                        borderRadius: '50%',
                        margin: 'auto',
                    }}
                    />
                ) : null;
                },
            },
            {
                title: 'Medication Name',
                dataIndex: 'medicationName',
                key: 'medicationName',
            },
            {
                title: 'Composition Name',
                dataIndex: 'compositionName',
                key: 'compositionName',
            },
            {
                title: 'Strength',
                dataIndex: 'strength',
                key: 'strength',
            },
            {
                title: 'Quantity',
                dataIndex: 'quantity',
                key: 'quantity',
            },
            {
                title: 'Expiry',
                dataIndex: 'expiry',
                key: 'expiry',
            },
            {
                title: 'Manufacturer',
                dataIndex: 'manufacturer',
                key: 'manufacturer',
            },
            {
              title: 'Total Cost',
              dataIndex: 'price',
              key: 'price',
              render: (price) => `₹${price}`,
            },
            {
              title: 'Pharmacy',
              dataIndex: 'pharmacyName',
              key: 'pharmacyName',
            }
            ]}
            bordered
            className="Medications-table"
            scroll={{ x: 'max-content' }}
            style={{ marginBottom: '16px' }}
        />)}            
        {!loading && !error && (
            <Button type="primary" icon={<MedicationSharp/>} className="register-button" onClick={() => navigate('/page/order-medicines')}>
                Buy Medications
            </Button>
        )}
        </Card>
    </div>
  );
};

export default Medications;
