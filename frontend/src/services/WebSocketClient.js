import { useState, useEffect, useCallback, useRef } from 'react';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

const useWebSocket = () => {
  const [client, setClient] = useState(null);
  const [isConnected, setIsConnected] = useState(false);
  const [lastMessage, setLastMessage] = useState(null);
  const [error, setError] = useState(null);
  const isDisconnectingRef = useRef(false);
  const stompClientRef = useRef(null);

  const disconnect = useCallback(() => {
    if (isDisconnectingRef.current) {
      console.log('[WebSocket] Already disconnecting, skipping redundant call');
      return Promise.resolve();
    }
    isDisconnectingRef.current = true;

    return new Promise((resolve) => {
      if (stompClientRef.current && stompClientRef.current.connected) {
        console.log('[WebSocket] Manually disconnecting');
        stompClientRef.current.disconnect(() => {
          console.log('[WebSocket] Disconnected');
          setIsConnected(false);
          setClient(null);
          stompClientRef.current = null;
          isDisconnectingRef.current = false;
          resolve();
        });
      } else {
        isDisconnectingRef.current = false;
        resolve();
      }
    });
  }, []);

  const connectWebSocket = useCallback((token) => {
    try {
      console.log('[WebSocket] Starting connection process');

      const BACKEND_URL = process.env.REACT_APP_BACKEND_URL || 'http://localhost:8081';
      console.log('[WebSocket] Using BACKEND_URL:', BACKEND_URL);

      console.log('[WebSocket] Creating SockJS instance with URL:', `${BACKEND_URL}/ws`);
      const socket = new SockJS(`${BACKEND_URL}/ws`);
      console.log('[WebSocket] SockJS instance created');

      console.log('[WebSocket] Creating Stomp client');
      const stompClient = Stomp.over(socket);
      console.log('[WebSocket] Stomp client created');
      stompClientRef.current = stompClient;

      const headers = token ? { Authorization: `Bearer ${token}` } : {};
      console.log('[WebSocket] Connection headers:', headers);

      console.log('[WebSocket] Initiating Stomp connection');
      stompClient.connect(headers,
        (frame) => {
          console.log('[WebSocket] Connection established successfully');
          console.log('[WebSocket] Connection frame:', frame);
          setIsConnected(true);
          setError(null);
          setClient(stompClient);

          console.log('[WebSocket] Subscribing to /topic/agreements');
          stompClient.subscribe('/topic/agreements', (message) => {
            console.log('[WebSocket] Message received:', message.body);
            setLastMessage(JSON.parse(message.body));
          });
        },
        (error) => {
          console.error('[WebSocket] Error occurred:', error);
          setError(`WebSocket error: ${error}`);
          setIsConnected(false);
        }
      );
    } catch (err) {
      console.error('[WebSocket] Error in connectWebSocket:', err);
      setError(`Error connecting to WebSocket: ${err.message}`);
    }
  }, []);

  useEffect(() => {
    console.log('[WebSocket] Setting up WebSocket connection');
    const token = localStorage.getItem('jwtToken');
    connectWebSocket(token);

    return () => {
      console.log('[WebSocket] Cleaning up WebSocket connection');
      if (stompClientRef.current) {
        disconnect();
      }
    };
  }, [connectWebSocket, disconnect]);

  const sendMessage = useCallback((destination, message) => {
    if (client && isConnected) {
      console.log('[WebSocket] Sending message:', message);
      client.send(destination, {}, JSON.stringify(message));
    } else {
      console.error('[WebSocket] Cannot send message: WebSocket is not connected');
      setError('WebSocket is not connected');
    }
  }, [client, isConnected]);

  return { isConnected, lastMessage, sendMessage, error, disconnect };
};

export default useWebSocket;

// Add this line to log the module loading
console.log('[WebSocket] WebSocketClient module loaded');
