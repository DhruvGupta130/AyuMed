import {useState} from "react";
import {Button, Form, Input, message, Modal, Upload} from "antd";
import {UploadOutlined} from "@ant-design/icons";
import axios from "axios";
import {hospitalURL} from "../../Api & Services/Api";
import PropTypes from "prop-types";
import {BACKEND_URL} from "../../configuration.js";

const LabTestModal = ({ labModalVisible, setLabModalVisible, recordId }) => {
  const [fileList, setFileList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [labResults, setLabResults] = useState({
    testName: '',
    testDate: '',
    result: '',
    filePath: '',
    notes: '',
  });

  const handleSubmit = async () => {
    if(!labResults.testName || !labResults.testDate || !labResults.result || !labResults.filePath || !labResults.notes){
      message.error("Please fill all required fields");
      return;
    }
    setLoading(true);
    try {
      const token = localStorage.getItem('token');
      const response = await axios.put(`${hospitalURL}/add-lab-results/${recordId}`, labResults, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      message.success(response.data.message);
      setLabModalVisible(false);
      setLabResults({
        testName: '',
        testDate: '',
        result: '',
        filePath: '',
        notes: '',
      });
      setFileList([]);
    } catch (error) {
      message.error("Failed to submit lab results.");
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleFileChange = async (info) => {
    if (info.file.status === "uploading") {
      setLoading(true);
    }
    if (info.file.status === "done") {
      message.success(`${info.file.name} file uploaded successfully`);
      const fileUrl = info.file.response || '';
      setFileList([{ ...info.file, url: fileUrl }]);
      labResults.filePath = fileUrl;
    } else if (info.file.status === "error") {
      message.error(`${info.file.name} file upload failed.`);
    }
    setLoading(false);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setLabResults((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  return (
    <Modal
      title="Add Lab Results"
      open={labModalVisible}
      onCancel={() => setLabModalVisible(false)}
      footer={null}
      scroll="vertical"
    >
      <Form layout="vertical" onFinish={handleSubmit}>
        <Form.Item
          label="Test Name"
          name="testName"
        >
          <Input
            name="testName"
            value={labResults.testName}
            onChange={handleInputChange}
          />
        </Form.Item>

        <Form.Item
          label="Test Date"
          name="testDate"
        >
          <Input
            name="testDate"
            type="date"
            value={labResults.testDate}
            onChange={handleInputChange}
          />
        </Form.Item>

        <Form.Item
          label="Result"
          name="result"
        >
          <Input
            name="result"
            value={labResults.result}
            onChange={handleInputChange}
          />
        </Form.Item>

        <Form.Item
          label="Upload File"
          name="filePath"
          valuePropName="fileList"
          getValueFromEvent={(e) => e && e.fileList}
        >
          <Upload
            listType="picture"
            fileList={fileList}
            action={`${BACKEND_URL}/upload-image`}
            onChange={handleFileChange}
            multiple={false}
          >
            <Button icon={<UploadOutlined />}>Upload File</Button>
          </Upload>
        </Form.Item>

        <Form.Item label="Notes" name="notes">
          <Input.TextArea
            rows={3}
            placeholder="Additional notes (optional)"
            name="notes"
            value={labResults.notes}
            onChange={handleInputChange}
          />
        </Form.Item>

        <Form.Item>
          <Button type="primary" htmlType="submit" block loading={loading}>
            Submit Lab Result
          </Button>
        </Form.Item>
      </Form>
    </Modal>
  );
};

LabTestModal.propTypes = {
  labModalVisible: PropTypes.bool.isRequired,
  setLabModalVisible: PropTypes.func.isRequired,
  recordId: PropTypes.string.isRequired,
};

export default LabTestModal;
