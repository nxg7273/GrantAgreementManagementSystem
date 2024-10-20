import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api, { setAuthToken } from '../../services/api';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post('/api/auth/signin', { username, password });
      const { accessToken } = response.data;
      localStorage.setItem('jwtToken', accessToken);
      setAuthToken(accessToken);
      setMessage('Login successful');
      navigate('/participant');
    } catch (error) {
      console.error('Login error:', error);
      if (error.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        setMessage(`Login failed: ${error.response.data.message || error.response.statusText}`);
      } else if (error.request) {
        // The request was made but no response was received
        setMessage('Login failed: No response from server. Please try again.');
      } else {
        // Something happened in setting up the request that triggered an Error
        setMessage(`Login failed: ${error.message}`);
      }
    }
  };

  return (
    <div style={{ maxWidth: '400px', margin: 'auto', marginTop: '32px' }}>
      <form onSubmit={handleLogin} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
        <h1 style={{ textAlign: 'center' }}>Login</h1>
        {message && <div style={{ color: message.includes('successful') ? 'green' : 'red' }}>{message}</div>}
        <div>
          <label htmlFor="username" style={{ display: 'block', marginBottom: '4px' }}>Username</label>
          <input
            id="username"
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            style={{ width: '100%', padding: '8px' }}
          />
        </div>
        <div>
          <label htmlFor="password" style={{ display: 'block', marginBottom: '4px' }}>Password</label>
          <input
            id="password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            style={{ width: '100%', padding: '8px' }}
          />
        </div>
        <button type="submit" style={{ padding: '8px', backgroundColor: '#3182ce', color: 'white', border: 'none', cursor: 'pointer' }}>
          Login
        </button>
      </form>
    </div>
  );
};

export default Login;
