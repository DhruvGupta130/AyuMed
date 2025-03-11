import  { useRef, useEffect, useState } from "react";
import { Bar, Pie } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  LineElement,
  PointElement,
} from "chart.js";

ChartJS.register(
  CategoryScale, 
  LinearScale,
  BarElement,
  ArcElement,    
  Title,
  Tooltip,
  Legend,
  LineElement,
  PointElement
);

import { Card, Row, Col, Statistic, Divider } from "antd";
import {
  UserOutlined,
  SolutionOutlined,
  CalendarOutlined,
  MedicineBoxOutlined,
} from "@ant-design/icons";
import axios from "axios";
import { URL } from "../Api & Services/Api";
import "../Styles/Dashboard.css";
import { Alert, CircularProgress } from "@mui/material";
import PropTypes from "prop-types";

// 📊 Dynamic Chart Wrapper
const DynamicChart = ({ ChartComponent, data }) => {
  const chartContainer = useRef(null);
  const [width, setWidth] = useState(400);

  useEffect(() => {
    const resizeObserver = new ResizeObserver(() => {
      if (chartContainer.current) {
        setWidth(chartContainer.current.offsetWidth);
      }
    });
    resizeObserver.observe(chartContainer.current);
    return () => resizeObserver.disconnect();
  }, []);

  return (
    <div ref={chartContainer} className="chart-container">
      <ChartComponent data={data} options={{ responsive: true, maintainAspectRatio: false, width }} />
    </div>
  );
};

// 📊 Analytics Dashboard Component
const AnalyticsDashboard = () => {
  const [statistics, setStatistics] = useState({});
  const [patientGrowth, setPatientGrowth] = useState({});
  const [averageDurationByDoctor, setAverageDurationByDoctor] = useState({});
  const [appointmentAnalysis, setAppointmentAnalysis] = useState({});
  const [demographics, setDemographics] = useState({});
  const [trends, setTrends] = useState({});
  const [departmentStats, setDepartmentStats] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch Data
  useEffect(() => {
    const fetchData = async () => {
      try {
        const [statsRes, perfRes, apptRes, demoRes, trendRes, departRes] = await Promise.all([
          axios.get(`${URL}/statistics`),
          axios.get(`${URL}/performance`),
          axios.get(`${URL}/appointments/analysis`),
          axios.get(`${URL}/demographics`),
          axios.get(`${URL}/trends`),
          axios.get(`${URL}/stats/departments`),
        ]);

        setStatistics(statsRes.data);
        setPatientGrowth(perfRes.data.patientGrowth || {});
        setAverageDurationByDoctor(perfRes.data.averageDurationByDoctor || {});
        setAppointmentAnalysis(apptRes.data);
        setDemographics(demoRes.data);
        setTrends(trendRes.data);
        setDepartmentStats(departRes.data);
      } catch (error) {
        console.error(error);
        setError(error.response.data.message || "Error fetching analytics data.");
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  if (loading) return <p style={{textAlign: 'center', margin: 50}}><CircularProgress/></p>;
  if (error) return (<Alert sx={{ display: "flex", justifyContent: "center", fontSize: "medium" }} severity="error">
      {error}
    </Alert>)

  const patientGrowthData = {
    labels: Object.keys(patientGrowth),
    datasets: [
      {
        label: "New Patients",
        data: Object.values(patientGrowth),
        backgroundColor: "rgba(54, 162, 235, 0.6)",
      },
    ],
  };

  const consultationDurationData = {
    labels: Object.keys(averageDurationByDoctor),
    datasets: [
      {
        label: "Avg Consultation Duration (mins)",
        data: Object.values(averageDurationByDoctor),
        backgroundColor: ["#FF6384", "#36A2EB", "#FFCE56"],
      },
    ],
  };

  
  const departmentChartData = {
    labels: Object.keys(appointmentAnalysis.departmentCounts || {}),
    datasets: [
      {
        label: "Appointments per Department",
        data: Object.values(appointmentAnalysis.departmentCounts || {}),
        backgroundColor: "rgba(75, 192, 192, 0.6)",
      },
    ],
  };

  const doctorDepartmentData = {
    labels: Object.keys(demographics.doctorDepartmentDistribution || {}),
    datasets: [
      {
        label: "Doctors per Department",
        data: Object.values(demographics.doctorDepartmentDistribution || {}),
        backgroundColor: "rgba(75, 192, 192, 0.6)",
      },
    ],
  };

  const patientAgeGroupData = {
    labels: Object.keys(demographics.patientAgeGroupDistribution || {}),
    datasets: [
      {
        label: "Patients Age Group",
        data: Object.values(demographics.patientAgeGroupDistribution || {}),
        backgroundColor: ["#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0"],
      },
    ],
  };

  const patientGenderData = {
    labels: Object.keys(demographics.patientGenderDistribution || {}),
    datasets: [
      {
        label: "Patient Gender",
        data: Object.values(demographics.patientGenderDistribution || {}),
        backgroundColor: ["#FF6384", "#36A2EB"],
      },
    ],
  };  
  
  const formatMonthlyAppointments = () => {
    if (!trends?.monthlyAppointments) return { labels: [], datasets: [] };
  
    return {
      labels: Object.keys(trends.monthlyAppointments),
      datasets: [
        {
          label: "Appointments per Month",
          data: Object.values(trends.monthlyAppointments),
          backgroundColor: "rgba(54, 162, 235, 0.6)",
        },
      ],
    };
  };
  
  const monthlyAppointmentsData = formatMonthlyAppointments();
  

  const formatWeeklyTrendsData = () => {
    if (!trends?.weeklyTrends) return { labels: [], datasets: [] };
  
    const daysOfWeek = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"];
    const statusTypes = new Set();
  
    Object.values(trends.weeklyTrends).forEach((statuses) => {
      Object.keys(statuses).forEach((status) => statusTypes.add(status));
    });
  
    const datasets = [...statusTypes].map((status) => ({
      label: status,
      data: daysOfWeek.map((day) => trends.weeklyTrends[day]?.[status] || 0),
      backgroundColor: status === "APPROVED" ? "#4caf50" : status === "PENDING" ? "#ff9800" : "#f44336",
    }));
  
    return { labels: daysOfWeek, datasets };
  };
  
  const weeklyTrendsData = formatWeeklyTrendsData();

  const departmentStatsData = {
    labels: Object.keys(departmentStats || {}),
    datasets: [
      {
        label: "Total Doctors per Department",
        data: Object.values(departmentStats || {}),
        backgroundColor: ["#4CAF50", "#FF9800", "#2196F3", "#9C27B0"],
        borderColor: "#fff",
        borderWidth: 1,
      },
    ],
  };
  

  return (
    <div className="dashboard-container">
      <Card title={
        <h3 className="dashboard-title" style={{textWrap: 'wrap', fontWeight: 600}}>📊 Analytics Dashboard</h3>
      }>

        {/* General Statistics */}
        <Row gutter={[24, 24]}>
          <Col xs={24} sm={12} md={6}>
            <Card>
              <Statistic title="Today's Appointments" value={statistics.todayAppointments} prefix={<CalendarOutlined />} />
            </Card>
          </Col>
          <Col xs={24} sm={12} md={6}>
            <Card>
              <Statistic title="Total Appointments" value={statistics.totalAppointments} prefix={<MedicineBoxOutlined />} />
            </Card>
          </Col>
          <Col xs={24} sm={12} md={6}>
            <Card>
              <Statistic title="Total Doctors" value={statistics.totalDoctors} prefix={<SolutionOutlined />} />
            </Card>
          </Col>
          <Col xs={24} sm={12} md={6}>
            <Card>
              <Statistic title="Total Patients" value={statistics.totalPatients} prefix={<UserOutlined />} />
            </Card>
          </Col>
        </Row>

        <Divider />

        <Row gutter={[24, 24]}>
          {/* Patient Growth Bar Chart */}
          <Col xs={24} md={12}>
            <Card title="📈 Patient Growth (Monthly)">
              <DynamicChart ChartComponent={Bar} data={patientGrowthData} />
            </Card>
          </Col>

          {/* Consultation Duration Pie Chart */}
          <Col xs={24} md={12}>
            <Card title="⏳ Avg Consultation Duration by Doctor">
              <DynamicChart ChartComponent={Pie} data={consultationDurationData} />
            </Card>
          </Col>
        </Row>

        <Divider />

        {/* Charts Section */}
        <h3 className="section-title">📈 Data Insights</h3>
        <Row gutter={[24, 24]}>

          <Col xs={24} md={12}>
            <Card title="Department-wise Appointments">
              <DynamicChart ChartComponent={Bar} data={departmentChartData} />
            </Card>
          </Col>

          <Col xs={24} md={12}>
            <Card title="Appointments Trend">
              <DynamicChart
                ChartComponent={Bar}
                data={{
                  labels: Object.keys(appointmentAnalysis.statusCounts || {}),
                  datasets: [{ label: "Appointments", data: Object.values(appointmentAnalysis.statusCounts || {}), backgroundColor: "rgba(255, 99, 132, 0.6)" }],
                }}
              />
            </Card>
          </Col>

          <Col xs={24} md={12}>
            <Card title="👨‍⚕️ Doctor Department Distribution">
              <DynamicChart ChartComponent={Bar} data={doctorDepartmentData} />
            </Card>
          </Col>

          <Col xs={24} md={12}>
            <Card title="📈 Patient Age Group">
              <DynamicChart ChartComponent={Pie} data={patientAgeGroupData} />
            </Card>
          </Col>

          <Col xs={24} md={12}>
            <Card title="🚻 Patient Gender">
              <DynamicChart ChartComponent={Pie} data={patientGenderData} />
            </Card>
          </Col>

          <Col xs={24} md={12}>
            <Card title="📅 Monthly Appointments">
              <DynamicChart
                ChartComponent={Bar}
                data={monthlyAppointmentsData}
                options={{
                  responsive: true,
                  maintainAspectRatio: false,
                  scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } },
                }}
              />
            </Card>
          </Col>

          <Col xs={24} md={12}>
            <Card title="📊 Weekly Trends (Status-wise)">
              <DynamicChart
                ChartComponent={Bar}
                data={weeklyTrendsData}
                options={{
                  responsive: true,
                  maintainAspectRatio: false,
                  scales: { x: { stacked: true }, y: { stacked: true } },
                }}
              />
            </Card>
          </Col>

          <Col xs={24} md={12}>
            <Card title="🏥 Doctors Per Department">
              <DynamicChart ChartComponent={Bar} data={departmentStatsData} />
            </Card>
          </Col>

        </Row>
      </Card>
    </div>
  );
};

DynamicChart.propTypes = {
  ChartComponent: PropTypes.elementType.isRequired,
  data: PropTypes.shape({
    labels: PropTypes.arrayOf(PropTypes.string).isRequired,
    datasets: PropTypes.arrayOf(
        PropTypes.shape({
          label: PropTypes.string,
          data: PropTypes.arrayOf(PropTypes.number).isRequired,
          backgroundColor: PropTypes.oneOfType([PropTypes.string, PropTypes.arrayOf(PropTypes.string)]),
        })
    ).isRequired,
  }).isRequired,
};

AnalyticsDashboard.propTypes = {
  statistics: PropTypes.shape({
    todayAppointments: PropTypes.number,
    totalAppointments: PropTypes.number,
    totalDoctors: PropTypes.number,
    totalPatients: PropTypes.number,
  }),
  patientGrowth: PropTypes.object,
  averageDurationByDoctor: PropTypes.object,
  appointmentAnalysis: PropTypes.shape({
    departmentCounts: PropTypes.object,
    statusCounts: PropTypes.object,
  }),
  demographics: PropTypes.shape({
    doctorDepartmentDistribution: PropTypes.object,
    patientAgeGroupDistribution: PropTypes.object,
    patientGenderDistribution: PropTypes.object,
  }),
  trends: PropTypes.shape({
    monthlyAppointments: PropTypes.object,
    weeklyTrends: PropTypes.object,
  }),
  departmentStats: PropTypes.object,
  loading: PropTypes.bool,
  error: PropTypes.string,
};

export default AnalyticsDashboard;
