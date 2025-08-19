import {useEffect, useState} from "react";
import axios from "axios";
import {useLocation} from "react-router-dom";
import {Box, CircularProgress, Container, Divider, Typography} from "@mui/material";
import SearchResultCard from "../Components/SearchResultCard.jsx";
import "../Styles/SearchPage.css";
import {BACKEND_URL} from "../configuration.js";

function SearchPage() {
  const [searchResults, setSearchResults] = useState({
    hospitals: [],
    pharmacies: [],
    doctors: [],
    medications: [],
  });
  const [searchTerm, setSearchTerm] = useState("");
  const [loading, setLoading] = useState(false);
  const location = useLocation();

  useEffect(() => {
    const keyword = new URLSearchParams(location.search).get("keyword");
    if (keyword) {
      setSearchTerm(keyword);
      fetchSearchResults(keyword);
    }
  }, [location.search]);

  const fetchSearchResults = async (keyword) => {
    setLoading(true);
    try {
      const response = await axios.get(`${BACKEND_URL}/search`, {
        params: { keyword },
      });
      setSearchResults(response.data);
      console.log(response.data);
    } catch (error) {
      console.error("Error fetching search results:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container className="search-page" maxWidth="lg">
      <Typography variant="h6" align="center" gutterBottom className="search-page-title">
        Search Results for: &#34;{searchTerm}&#34;
      </Typography>

      {loading ? (
        <Box className="loading-spinner">
          <CircularProgress />
        </Box>
      ) : (
        <div>
          {searchResults.hospitals?.length > 0 && (
            <div className="card-section">
              <h2 className="section-title">
                Hospitals
              </h2>
              <Box className="card-container">
                {searchResults.hospitals.map((hospital) => (
                  <SearchResultCard key={hospital.id} result={hospital} type="Hospital" />
                ))}
              </Box>
              <Divider className="section-divider" />
            </div>
          )}

          {searchResults.pharmacies?.length > 0 && (
            <div className="card-section">
              <h2 className="section-title">
                Pharmacies
              </h2>
              <Box className="card-container">
                {searchResults.pharmacies.map((pharmacy) => (
                  <SearchResultCard key={pharmacy.id} result={pharmacy} type="Pharmacy" />
                ))}
              </Box>
              <Divider className="section-divider" />
            </div>
          )}

          {searchResults.doctors?.length > 0 && (
            <div className="card-section">
             <h2 className="section-title">
                Doctors
              </h2>
              <Box className="card-container">
                {searchResults.doctors.map((doctor) => (
                  <SearchResultCard key={doctor.id} result={doctor} type="Doctor" />
                ))}
              </Box>
              <Divider className="section-divider" />
            </div>
          )}

          {searchResults.medications?.length > 0 && (
            <div className="card-section">
              <h2 className="section-title">
                Medications
              </h2>
              <Box className="card-container">
                {searchResults.medications.map((medication) => (
                  <SearchResultCard key={medication.id} result={medication} type="Medication" />
                ))}
              </Box>
              <Divider className="section-divider" />
            </div>
          )}

          {searchResults.hospitals.length === 0 &&
            searchResults.doctors.length === 0 &&
            searchResults.medications.length === 0 && (
              <Typography variant="h6" align="center" className="no-results">
                No results found for your search.
              </Typography>
            )}
        </div>
      )}
    </Container>
  );
}

export default SearchPage;
