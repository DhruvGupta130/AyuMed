import { Link } from 'react-router-dom';
import '../Styles/NotFound.css';

const NotFound = () => {
  return (
    <div className="not-found-container">
      <div className="not-found-content">
        <h1 className="not-found-title">404</h1>
        <p className="not-found-message">Sorry, the page you&apos;re looking for cannot be found.</p>
        <Link to="/" className="not-found-link">
          Go Back to Home
        </Link>
      </div>
    </div>
  );
};

export default NotFound;
