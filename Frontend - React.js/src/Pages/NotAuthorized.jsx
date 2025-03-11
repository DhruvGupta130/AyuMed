import { Link } from 'react-router-dom';
import '../Styles/NotAuthorized.css';

const NotAuthorized = () => {
  return (
    <div className="unauthorized-container">
      <div className="unauthorized-content">
        <h1 className="unauthorized-title">403</h1>
        <p className="unauthorized-message">Oops! You don&apos;t have permission to access this page.</p>
        <Link to="/" className="unauthorized-link">
          Return to Home
        </Link>
      </div>
    </div>
  );
};

export default NotAuthorized;
