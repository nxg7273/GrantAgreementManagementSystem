import React, { useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Link, Navigate, useLocation } from 'react-router-dom';
import ParticipantDashboard from './components/ParticipantPortal/Dashboard';
import AgreementList from './components/ParticipantPortal/AgreementList';
import AgreementDetails from './components/ParticipantPortal/AgreementDetails';
import AdminDashboard from './components/AdminPortal/Dashboard';
import GrantManagement from './components/AdminPortal/GrantManagement';
import AgreementManagement from './components/AdminPortal/AgreementManagement';
import Login from './components/Auth/Login';
import useWebSocket from './services/WebSocketClient';
import { setAuthToken } from './services/api';

class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false };
  }

  static getDerivedStateFromError(error) {
    return { hasError: true };
  }

  componentDidCatch(error, errorInfo) {
    console.error('Error caught by boundary:', error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      return <h1>Something went wrong. Please try refreshing the page.</h1>;
    }
    return this.props.children;
  }
}

function LocationLogger() {
  const location = useLocation();
  React.useEffect(() => {
    console.log('Current location:', location);
  }, [location]);
  return null;
}

function AppContent() {
  const location = useLocation();
  console.log('AppContent: Current location:', location);

  return (
    <div className="App">
      <nav>
        <ul>
          <li><Link to="/login">Login</Link></li>
          <li><Link to="/participant/dashboard">Participant Dashboard</Link></li>
          <li><Link to="/admin/dashboard">Admin Dashboard</Link></li>
        </ul>
      </nav>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/participant" element={<Navigate to="/participant/dashboard" replace />} />
        <Route path="/participant/dashboard" element={<ParticipantDashboard />} />
        <Route path="/participant/agreements" element={<AgreementList />} />
        <Route path="/participant/agreements/:id" element={<AgreementDetails />} />
        <Route path="/admin/dashboard" element={<AdminDashboard />} />
        <Route path="/admin/grants" element={<GrantManagement />} />
        <Route path="/admin/agreements" element={<AgreementManagement />} />
        <Route path="/" element={<Navigate to="/login" replace />} />
      </Routes>
    </div>
  );
}

function WebSocketWrapper({ children }) {
  const { isConnected, lastMessage, error } = useWebSocket('/ws');
  React.useEffect(() => {
    console.log('[WebSocket] Connection status:', isConnected ? 'Connected' : 'Disconnected');
    if (isConnected) {
      console.log('[WebSocket] Successfully connected to:', process.env.REACT_APP_BACKEND_URL);
    }
    if (lastMessage) {
      console.log('[WebSocket] Last message received:', lastMessage);
    }
    if (error) {
      console.error('[WebSocket] Error:', error);
    }
  }, [isConnected, lastMessage, error]);

  return (
    <div>
      <p>WebSocket Status: {isConnected ? 'Connected' : 'Disconnected'}</p>
      {children}
    </div>
  );
}

function App() {
  console.log('App component rendering...');
  console.log('REACT_APP_BACKEND_URL:', process.env.REACT_APP_BACKEND_URL);

  useEffect(() => {
    const token = localStorage.getItem('jwtToken');
    if (token) {
      setAuthToken(token);
    }
  }, []);

  return (
    <ErrorBoundary>
      <Router>
        <WebSocketWrapper>
          <AppContent />
        </WebSocketWrapper>
      </Router>
    </ErrorBoundary>
  );
}

export default App;
