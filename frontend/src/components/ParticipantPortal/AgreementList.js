import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

const AgreementList = () => {
  const [agreements, setAgreements] = useState([]);

  useEffect(() => {
    const fetchAgreements = async () => {
      try {
        const response = await axios.get('/api/agreements');
        setAgreements(response.data);
      } catch (error) {
        console.error('Error fetching agreements:', error);
      }
    };

    fetchAgreements();
  }, []);

  return (
    <div>
      <h2>Agreements</h2>
      <ul>
        {agreements.map(agreement => (
          <li key={agreement.id}>
            <Link to={`/participant/agreements/${agreement.id}`}>
              Agreement {agreement.id} - Status: {agreement.status}
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default AgreementList;
