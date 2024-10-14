import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import ParticipantDashboard from './components/ParticipantPortal/Dashboard';
import AgreementList from './components/ParticipantPortal/AgreementList';
import AgreementDetails from './components/ParticipantPortal/AgreementDetails';
import AdminDashboard from './components/AdminPortal/Dashboard';
import GrantManagement from './components/AdminPortal/GrantManagement';
import AgreementManagement from './components/AdminPortal/AgreementManagement';

class ErrorBoundary extends Component {
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

function App() {
  console.log('App component rendering...');
  return (
    <ErrorBoundary>
      <Router>
        <div className="App">
          <nav>
            <ul>
              <li><Link to="/participant">Participant Portal</Link></li>
              <li><Link to="/admin">Admin Portal</Link></li>
            </ul>
          </nav>

          <Routes>
            <Route path="/participant" element={<ParticipantDashboard />} />
            <Route path="/participant/agreements" element={<AgreementList />} />
            <Route path="/participant/agreements/:id" element={<AgreementDetails />} />
            <Route path="/admin" element={<AdminDashboard />} />
            <Route path="/admin/grants" element={<GrantManagement />} />
            <Route path="/admin/agreements" element={<AgreementManagement />} />
          </Routes>
        </div>
      </Router>
    </ErrorBoundary>
  );
}

export default App;
