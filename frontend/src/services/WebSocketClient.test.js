import React from 'react';
import useWebSocket from './WebSocketClient';
import { render, act, screen, waitFor } from '@testing-library/react';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

jest.mock('sockjs-client');
jest.mock('@stomp/stompjs');

const TestComponent = () => {
  const { isConnected, sendMessage, lastMessage, disconnect } = useWebSocket();
  return (
    <div>
      <div data-testid="status">{isConnected ? 'Connected' : 'Disconnected'}</div>
      <button onClick={() => sendMessage('/test', 'Test message')}>Send Message</button>
      <button onClick={disconnect}>Disconnect</button>
      <div data-testid="lastMessage">{JSON.stringify(lastMessage)}</div>
    </div>
  );
};

describe('WebSocketClient', () => {
  let mockStompClient;
  let mockDisconnect;

  beforeEach(() => {
    mockDisconnect = jest.fn((callback) => {
      setTimeout(() => {
        callback();
      }, 0);
    });

    mockStompClient = {
      connect: jest.fn((_, successCallback) => {
        setTimeout(() => {
          successCallback();
        }, 0);
      }),
      subscribe: jest.fn((_, callback) => callback({ body: JSON.stringify({ type: 'TEST', payload: 'Test data' }) })),
      send: jest.fn(),
      disconnect: mockDisconnect,
      connected: true,
    };

    Stomp.over.mockReturnValue(mockStompClient);
  });

  it('should connect to WebSocket', async () => {
    render(<TestComponent />);

    await waitFor(() => {
      expect(screen.getByText('Connected')).toBeInTheDocument();
    });

    expect(SockJS).toHaveBeenCalledWith('http://localhost:8080/ws');
    expect(Stomp.over).toHaveBeenCalled();
    expect(mockStompClient.connect).toHaveBeenCalled();
  });

  it('should send messages', async () => {
    render(<TestComponent />);

    await waitFor(() => {
      expect(screen.getByText('Connected')).toBeInTheDocument();
    });

    act(() => {
      screen.getByText('Send Message').click();
    });

    expect(mockStompClient.send).toHaveBeenCalledWith('/test', {}, JSON.stringify('Test message'));
  });

  it('should handle incoming messages', async () => {
    render(<TestComponent />);

    await waitFor(() => {
      expect(screen.getByText('Connected')).toBeInTheDocument();
    });

    expect(mockStompClient.subscribe).toHaveBeenCalled();
    expect(screen.getByTestId('lastMessage')).toHaveTextContent('{"type":"TEST","payload":"Test data"}');
  });

  it('should close connection on unmount', async () => {
    const { unmount } = render(<TestComponent />);

    await waitFor(() => {
      expect(screen.getByText('Connected')).toBeInTheDocument();
    });

    act(() => {
      unmount();
    });

    await waitFor(() => {
      expect(mockDisconnect).toHaveBeenCalled();
    });

    await waitFor(() => {
      expect(screen.queryByText('Connected')).not.toBeInTheDocument();
    });
  });

  it('should disconnect when disconnect button is clicked', async () => {
    render(<TestComponent />);

    await waitFor(() => {
      expect(screen.getByText('Connected')).toBeInTheDocument();
    });

    act(() => {
      screen.getByText('Disconnect').click();
    });

    await waitFor(() => {
      expect(mockDisconnect).toHaveBeenCalled();
    });

    await waitFor(() => {
      expect(screen.getByText('Disconnected')).toBeInTheDocument();
    });
  });

  it('should not call disconnect multiple times', async () => {
    render(<TestComponent />);

    await waitFor(() => {
      expect(screen.getByText('Connected')).toBeInTheDocument();
    });

    act(() => {
      screen.getByText('Disconnect').click();
      screen.getByText('Disconnect').click();
    });

    await waitFor(() => {
      expect(mockDisconnect).toHaveBeenCalledTimes(1);
    });
  });
});
