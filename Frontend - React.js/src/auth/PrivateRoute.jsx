import PropTypes from 'prop-types';
import { Navigate } from 'react-router-dom';

const PrivateRoute = ({ children, roles }) => {
  const isAuthenticated = localStorage.getItem('isAuthenticated') === "true";
  const userRole = localStorage.getItem('role');

  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }

  if (roles && !roles.includes(userRole)) {
    return <Navigate to="/not-authorized" />;
  }

  return children; 
  
};

PrivateRoute.propTypes = {
  children: PropTypes.node.isRequired,
  roles: PropTypes.arrayOf(PropTypes.string),
};

export default PrivateRoute;
