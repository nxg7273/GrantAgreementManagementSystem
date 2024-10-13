import React from 'react';
import { BrowserRouter as Router, Route, Switch, Link } from 'react-router-dom';
import ParticipantDashboard from './components/ParticipantPortal/Dashboard';
import AgreementList from './components/ParticipantPortal/AgreementList';
import AgreementDetails from './components/ParticipantPortal/AgreementDetails';
import AdminDashboard from './components/AdminPortal/Dashboard';
import GrantManagement from './components/AdminPortal/GrantManagement';
import AgreementManagement from './components/AdminPortal/AgreementManagement';

function App() {
  return (
    <Router>
      <div className="App">
        <nav>
          <ul>
            <li><Link to="/participant">Participant Portal</Link></li>
            <li><Link to="/admin">Admin Portal</Link></li>
          </ul>
        </nav>

        <Switch>
          <Route path="/participant" exact component={ParticipantDashboard} />
          <Route path="/participant/agreements" exact component={AgreementList} />
          <Route path="/participant/agreements/:id" component={AgreementDetails} />
          <Route path="/admin" exact component={AdminDashboard} />
          <Route path="/admin/grants" component={GrantManagement} />
          <Route path="/admin/agreements" component={AgreementManagement} />
        </Switch>
      </div>
    </Router>
  );
}

export default App;
