import { useEffect, useState } from "react";
import Doctor from "../Assets/doctor-picture.png";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCalendarCheck, faAngleUp } from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from "react-router-dom";
import "../Styles/Hero.css";

function Hero() {
  const navigate = useNavigate();
  const [goUp, setGoUp] = useState(false);

  const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const handleBookAppointmentClick = () => {
    navigate("/page/appointment/book");
  };

  useEffect(() => {
    const onPageScroll = () => {
      if (window.scrollY > 600) {
        setGoUp(true);
      } else {
        setGoUp(false);
      }
    };
    window.addEventListener("scroll", onPageScroll);

    return () => {
      window.removeEventListener("scroll", onPageScroll);
    };
  }, []);

  return (
    <div className="section-container">
      <div className="hero-section">
        <div className="text-section">
          <p className="text-headline">🌿 Prioritize Your Health</p>
          <h2 className="text-title">
            Find Trusted Doctors and Book Your Appointments
          </h2>
          <p className="text-description">
            Get professional healthcare advice, prescriptions, and medical
            notes from certified doctors — all from the comfort of your home.
            Quality care within minutes.
          </p>
          <button
            className="text-appointment-btn"
            type="button"
            onClick={handleBookAppointmentClick}
          >
            <FontAwesomeIcon icon={faCalendarCheck} /> Book an Appointment
          </button>
          <div className="text-stats">
            <div className="text-stats-container">
              <p>145k+</p>
              <p>Patients Treated</p>
            </div>

            <div className="text-stats-container">
              <p>5000+</p>
              <p>Best Hospitals</p>
            </div>

            <div className="text-stats-container">
              <p>1000+</p>
              <p>Pharmacies</p>
            </div>
          </div>
        </div>

        <div className="hero-image-section">
          <img className="hero-image1" src={Doctor} alt="Doctor" />
        </div>
      </div>

      <div
        onClick={scrollToTop}
        className={`scroll-up ${goUp ? "show-scroll" : ""}`}
      >
        <FontAwesomeIcon icon={faAngleUp} />
      </div>
    </div>
  );
}

export default Hero;
