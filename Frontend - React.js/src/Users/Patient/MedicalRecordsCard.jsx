import PropTypes from "prop-types";

const MedicalRecordsCard = ({ medicalHistories, medications }) => {
  return (
    <div className="medical-records-container">
      <div className="medical-records-section">
        <div className="horizontal-records">
          {medicalHistories.map((history) => (
            <div className="record-card" key={history.id}>
              <h3>Problem: {history.problems}</h3>
              <p><strong>Diagnosis:</strong> {history.diagnosisDetails}</p>
              <p><strong>Treatment Plan:</strong> {history.treatmentPlan}</p>
              <p><strong>Doctor:</strong> {history.doctorName}</p>
            </div>
          ))}
        </div>
      </div>

      {/* Medication Records Section */}
      <div className="medical-records-section">
        <h2 className="section-title">Medications</h2>
        <div className="horizontal-records">
          <div className="record-card medications">
          {medications.map((medication, index) => (
            <ul key={index}>
              <li>{medication}</li>
            </ul>
          ))}
          </div>
        </div>
      </div>
    </div>
  );
};

MedicalRecordsCard.propTypes = {
  medicalHistories: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number.isRequired,
      problems: PropTypes.string.isRequired,
      diagnosisDetails: PropTypes.string.isRequired,
      treatmentPlan: PropTypes.string.isRequired,
      doctorName: PropTypes.string.isRequired,
    })
  ).isRequired,
  medications: PropTypes.arrayOf(PropTypes.string).isRequired,
};

export default MedicalRecordsCard;
