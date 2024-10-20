import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import WebSocketClient from '../../services/WebSocketClient';

const Dashboard = () => {
  const [pendingAgreements, setPendingAgreements] = useState([]);
  const [recentAgreements, setRecentAgreements] = useState([]);

  const { isConnected, lastMessage } = WebSocketClient('/ws');

  const fetchAgreements = async () => {
    try {
      const pendingResponse = await api.get('/agreements?status=pending');
      setPendingAgreements(pendingResponse.data);

      const recentResponse = await api.get('/agreements?limit=5');
      setRecentAgreements(recentResponse.data);
    } catch (error) {
      console.error('Error fetching agreements:', error);
    }
  };

  useEffect(() => {
    fetchAgreements();
  }, []);

  useEffect(() => {
    if (isConnected) {
      console.log('WebSocket connected successfully');
    } else {
      console.log('WebSocket disconnected');
    }
  }, [isConnected]);

  useEffect(() => {
    if (lastMessage) {
      console.log('Received WebSocket message:', lastMessage);
      const message = JSON.parse(lastMessage);
      if (message.type === 'AGREEMENT_UPDATE') {
        fetchAgreements();
      }
    }
  }, [lastMessage]);

  return (
    <div style={{ padding: '20px' }}>
      <h1 style={{ marginBottom: '24px' }}>Participant Dashboard</h1>
      <p style={{ marginBottom: '16px' }}>WebSocket Status: {isConnected ? 'Connected' : 'Disconnected'}</p>
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <div style={{ flex: 1, marginRight: '16px' }}>
          <h2 style={{ marginBottom: '16px' }}>Pending Agreements</h2>
          {pendingAgreements.map((agreement) => (
            <div key={agreement.id} style={{ padding: '12px', backgroundColor: '#f0f0f0', borderRadius: '4px', marginBottom: '8px' }}>
              <p style={{ fontWeight: 'bold' }}>{agreement.name}</p>
              <p style={{ fontSize: '14px' }}>Status: {agreement.status}</p>
              <button style={{ marginTop: '8px', padding: '4px 8px', backgroundColor: '#3182ce', color: 'white', border: 'none', borderRadius: '4px' }}>
                View Details
              </button>
            </div>
          ))}
        </div>
        <div style={{ flex: 1 }}>
          <h2 style={{ marginBottom: '16px' }}>Recent Agreements</h2>
          {recentAgreements.map((agreement) => (
            <div key={agreement.id} style={{ padding: '12px', backgroundColor: '#f0f0f0', borderRadius: '4px', marginBottom: '8px' }}>
              <p style={{ fontWeight: 'bold' }}>{agreement.name}</p>
              <p style={{ fontSize: '14px' }}>Status: {agreement.status}</p>
              <button style={{ marginTop: '8px', padding: '4px 8px', backgroundColor: '#38a169', color: 'white', border: 'none', borderRadius: '4px' }}>
                View Agreement
              </button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
