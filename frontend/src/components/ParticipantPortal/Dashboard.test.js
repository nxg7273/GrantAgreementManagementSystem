import React from 'react';
import { render, screen, waitFor, act } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import Dashboard from './Dashboard';
import * as api from '../../services/api';
import WebSocketClient from '../../services/WebSocketClient';

jest.mock('../../services/api');
jest.mock('../../services/WebSocketClient');

describe('Dashboard Component', () => {
  beforeEach(() => {
    api.get.mockImplementation((url) => {
      if (url === '/agreements?status=pending') {
        return Promise.resolve({ data: [{ id: 1, name: 'Pending Agreement 1', status: 'pending' }] });
      } else if (url === '/agreements?limit=5') {
        return Promise.resolve({ data: [{ id: 2, name: 'Recent Agreement 1', status: 'active' }] });
      }
    });

    WebSocketClient.mockImplementation(() => ({
      isConnected: true,
      lastMessage: null,
    }));
  });

  it('renders dashboard with agreement lists', async () => {
    await act(async () => {
      render(
        <BrowserRouter>
          <Dashboard />
        </BrowserRouter>
      );
    });

    await waitFor(() => {
      expect(screen.getByText('Participant Dashboard')).toBeInTheDocument();
      expect(screen.getByText('Pending Agreements')).toBeInTheDocument();
      expect(screen.getByText('Recent Agreements')).toBeInTheDocument();
    });

    expect(screen.getByText('Pending Agreement 1')).toBeInTheDocument();
    expect(screen.getByText('Recent Agreement 1')).toBeInTheDocument();
  });

  it('displays WebSocket status', async () => {
    await act(async () => {
      render(
        <BrowserRouter>
          <Dashboard />
        </BrowserRouter>
      );
    });

    expect(screen.getByText('WebSocket Status: Connected')).toBeInTheDocument();
  });

  it('handles API error', async () => {
    api.get.mockRejectedValue(new Error('API Error'));

    await act(async () => {
      render(
        <BrowserRouter>
          <Dashboard />
        </BrowserRouter>
      );
    });

    await waitFor(() => {
      expect(screen.getByText('Error fetching agreements:')).toBeInTheDocument();
    });
  });
});
