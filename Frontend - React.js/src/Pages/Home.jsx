import Hero from "../Components/Hero.jsx";
import Info from "../Components/Info.jsx";
import Footer from "../Components/Footer.jsx";
import "../Components/AnalyticsDashboard.jsx";
import DepartmentsSection from "../Components/DepartmentsSection.jsx";

function Home() {
  return (
    <div className="home-section">
      {/* Hero Section */}
      <Hero />

      {/* Main Features Information */}
      <Info />
      <DepartmentsSection/>

      {/* Footer Section */}
      <Footer />
    </div>
  );
}

export default Home;
