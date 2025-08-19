import {useCallback, useEffect, useState} from 'react';
import { Modal, Table, message } from 'antd';
import axios from 'axios';
import { hospitalURL } from '../../Api & Services/Api';
import { Button } from '@mui/material';
import { Download } from '@mui/icons-material';
import PropTypes from "prop-types";

const LabTestView = ({ visible, setVisible, medicalId}) => {

    const [loading, setLoading] = useState(false);
    const [testResults, setTestResults] = useState([]);

    const fetchLabTests = useCallback(async () => {
        setLoading(true);
        try {
            const token = localStorage.getItem('token');
            const response = await axios.get(`${hospitalURL}/lab-tests/${medicalId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setTestResults(response.data);
            console.log(response.data);
        } catch (error) {
            message.error("Error fetching Lab Tests. Please try again later.");
            console.error('Error fetching Lab Tests:', error);
        } finally{
            setLoading(false);
        }
    }, [medicalId]);

    useEffect(() => {
        if (visible && medicalId) {
          fetchLabTests();
        }
      }, [visible, medicalId, fetchLabTests]);

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

  const columns = [
    {
      title: 'Test Name',
      dataIndex: 'testName',
      key: 'testName',
    },
    {
      title: 'Test Date',
      dataIndex: 'testDate',
      key: 'testDate',
    },
    {
      title: 'Result',
      dataIndex: 'result',
      key: 'result',
    },
    {
      title: 'Notes',
      dataIndex: 'notes',
      key: 'notes',
    },
    {
        title: 'File',
        key: 'filePath',
        render: (_, record) => (
            <Button color='success' variant='outlined' onClick={() => downloadFile(record.id)} endIcon={<Download/>}>
                Download Lab Report
            </Button>
        ),
      },
  ];

  return (
    <Modal
      title="Lab Test Details"
      open={visible}
      footer={null}
      onCancel={() => setVisible(false)}
      width={1000}
      loading={loading}
      scroll="vertical"
    >
      <Table
        dataSource={testResults.map(test => ({
            ...test,
            key: test.id,
        }))}
        columns={columns}
        bordered
        loading={loading}
        pagination={{ pageSize: 5 }}
        scroll={{ x: 800 }} 
        rowKey="testName"
      />
    </Modal>
  );
};

LabTestView.propTypes = {
    visible: PropTypes.bool.isRequired,
    setVisible: PropTypes.func.isRequired,
    medicalId: PropTypes.string.isRequired,
};

export default LabTestView;
