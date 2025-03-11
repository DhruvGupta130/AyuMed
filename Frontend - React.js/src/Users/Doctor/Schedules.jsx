import {useState, useEffect, useCallback} from "react";
import {
  Table,
  Checkbox,
  Input,
  Select,
  Button,
  Typography,
  Row,
  Col,
  message,
  Spin,
  Card,
} from "antd";
import { CalendarFilled, ClockCircleFilled, PlusCircleOutlined, SaveOutlined, SunFilled } from "@ant-design/icons";
import axios from "axios";
import { doctorURL } from "../../Api & Services/Api.js";
import { ClearAllOutlined } from "@mui/icons-material";

const {Title } = Typography;

const Schedules = () => {
  const [schedules, setSchedules] = useState([
    { id: "", startTime: "", endTime: "", dayOfWeek: "", date: "", checked: false },
  ]);
  const [selectAll, setSelectAll] = useState(false);
  const [loading, setLoading] = useState(false);
  const [selectedSchedules, setSelectedSchedules] = useState([]);
  const [sortColumn, setSortColumn] = useState('date'); 
  const [sortDirection, setSortDirection] = useState('asc');
  const fetchSchedules = useCallback(async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`${doctorURL}/schedules?sortBy=${sortColumn}&sortDirection=${sortDirection}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setSchedules(response.data.map((schedule) => ({ ...schedule, checked: false })));
    } catch (error) {
      console.error("Failed to fetch schedules:", error);
      message.error("Could not fetch schedules.");
    } finally {
      setLoading(false);
    }
  }, [sortColumn, sortDirection]);

  useEffect(() => {
    fetchSchedules();
  }, [fetchSchedules, sortColumn, sortDirection]); // Fetch schedules whenever sort options change

  useEffect(() => {
    setSelectedSchedules(schedules.filter((schedule) => schedule.checked));
  }, [schedules]);

  useEffect(() => {
    setSelectAll(schedules.length === selectedSchedules.length && schedules.length !== 0);
  }, [schedules, selectedSchedules]);

  const handleScheduleChange = (index, field, value) => {
    const updatedSchedules = [...schedules];
    updatedSchedules[index][field] = value;
    setSchedules(updatedSchedules);
  };

  const toggleCheckbox = (index) => {
    const updatedSchedules = [...schedules];
    updatedSchedules[index].checked = !updatedSchedules[index].checked;
    setSchedules(updatedSchedules);
  };

  const addScheduleRow = () => {
    setSchedules([...schedules, { startTime: "", endTime: "", dayOfWeek: "", date: "", checked: true }]);
  };

  const toggleSelectAll = () => {
    const updatedSchedules = schedules.map((schedule) => ({ ...schedule, checked: !selectAll }));
    setSchedules(updatedSchedules);
    setSelectAll(!selectAll);
  };

  const handleSubmit = async () => {
    if (selectedSchedules.length === 0) {
      message.warning("Please select at least one schedule to submit.");
      return;
    }
    const hasEmptyFields = selectedSchedules.some(
      (schedule) => !schedule.startTime || !schedule.endTime || !schedule.dayOfWeek
    );

    if (hasEmptyFields) {
      message.error("Please fill in all required fields.");
      return;
    }

    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.post(`${doctorURL}/schedules`, selectedSchedules, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      message.success(response.data.message || "Schedules added successfully!");
      setSelectedSchedules([]);
      await fetchSchedules();
    } catch (error) {
      console.error(error);
      message.error(error?.response?.data?.message || "Failed to add schedules.");
    } finally {
      setLoading(false);
    }
  };

  const clearAllSchedules = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.delete(`${doctorURL}/schedules`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        data: selectedSchedules,
      });
      message.success(response.data.message);
      setSelectedSchedules([]);
      await fetchSchedules();
    } catch (error) {
      message.error(error?.response?.data?.message || "Unable to clear schedules");
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleSortChange = (columnKey, direction) => {
    if(direction === 'descend') direction = "desc";
    else direction = "asc";
    console.log(columnKey, direction);
    setSortColumn(columnKey);
    setSortDirection(direction);
  };

  return (
    <div className="profile-container">
      <Card title={
        <Title level={3}>
          Schedules
        </Title>
      } >
        {loading ? (
          <Spin tip="Loading Schedule Data..." size="large" style={{ display: "block", margin: "auto" }} />
        ) : (
          <Table
            rowKey="id"
            dataSource={schedules}
            pagination={{ pageSize: 10 }}
            bordered
            scroll={{ x: 800 }}
            onChange={(_pagination, _filters, sorter) => handleSortChange(sorter.column.dataIndex, sorter.order)}
          >
            <Table.Column
              title={<Checkbox checked={selectAll} onChange={toggleSelectAll} />}
              render={(_text, record, index) => (
                <Checkbox checked={record.checked} onChange={() => toggleCheckbox(index)} />
              )}
            />
            <Table.Column
              title={<strong>Start Time <ClockCircleFilled /></strong>}
              dataIndex="startTime"
              sorter
              render={(_text, record, index) => (
                <Input
                  type="time"
                  value={record.startTime}
                  onChange={(e) => handleScheduleChange(index, "startTime", e.target.value)}
                  required
                />
              )}
            />
            <Table.Column
              title={<strong>End Time <ClockCircleFilled /></strong>}
              dataIndex="endTime"
              sorter
              render={(text, record, index) => (
                <Input
                  type="time"
                  value={record.endTime}
                  onChange={(e) => handleScheduleChange(index, "endTime", e.target.value)}
                  required
                />
              )}
            />
            <Table.Column
              title={<strong>Day of Week <SunFilled /></strong>}
              dataIndex="dayOfWeek"
              sorter
              render={(text, record, index) => (
                <Select
                  value={record.dayOfWeek}
                  onChange={(e) => handleScheduleChange(index, "dayOfWeek", e)}
                  required
                >
                  <Select.Option value="" disabled>Select Day</Select.Option>
                  <Select.Option value="MONDAY">Monday</Select.Option>
                  <Select.Option value="TUESDAY">Tuesday</Select.Option>
                  <Select.Option value="WEDNESDAY">Wednesday</Select.Option>
                  <Select.Option value="THURSDAY">Thursday</Select.Option>
                  <Select.Option value="FRIDAY">Friday</Select.Option>
                  <Select.Option value="SATURDAY">Saturday</Select.Option>
                  <Select.Option value="SUNDAY">Sunday</Select.Option>
                </Select>
              )}
            />
            <Table.Column
              title={<strong>Date (Optional) <CalendarFilled /></strong>}
              dataIndex="date"
              sorter
              render={(text, record, index) => (
                <Input
                  type="date"
                  value={record.date}
                  onChange={(e) => handleScheduleChange(index, "date", e.target.value)}
                />
              )}
            />
          </Table>
        )}

        <Row justify="space-between" wrap style={{ marginTop: 20 }}>
          <Col lg={4} xs={24} md={6}>
            <Button
              icon={<PlusCircleOutlined />}
              onClick={addScheduleRow}
              type="primary"
              style={{marginBottom: 4}}
              block
            >
              Add Schedule
            </Button>
          </Col>
          <Col lg={4} md={6}>
            <Button
              icon={<ClearAllOutlined />}
              onClick={clearAllSchedules}
              type="primary"
              style={{marginTop: 4}}
              danger
              block
            >
              {selectedSchedules.length ? `Remove Selected` : `Clear All`}
            </Button>
          </Col>
          <Col lg={4} md={6}>
            <Button
              icon={<SaveOutlined />}
              onClick={handleSubmit}
              type="primary"
              style={{marginTop: 4, backgroundColor: '#389e0d'}}
              block
            >
              Update
            </Button>
          </Col>
        </Row>
      </Card>
    </div>
  );
};

export default Schedules;
