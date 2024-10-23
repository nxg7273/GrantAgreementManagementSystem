package com.grantmanagement.websocket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketTest {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketTest.class);

    @LocalServerPort
    private int port;

    private WebSocketStompClient stompClient;

    @BeforeEach
    public void setup() {
        logger.info("Setting up WebSocketTest");
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        SockJsClient sockJsClient = new SockJsClient(transports);

        this.stompClient = new WebSocketStompClient(sockJsClient);
        logger.debug("WebSocketStompClient initialized");
    }

    @Test
    public void testWebSocketConnection() throws Exception {
        logger.info("Starting testWebSocketConnection");
        CompletableFuture<byte[]> resultFuture = new CompletableFuture<>();

        logger.debug("Attempting to connect to WebSocket at ws://localhost:{}/ws", port);
        StompSession session = stompClient
                .connect(String.format("ws://localhost:%d/ws", port), new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);
        logger.debug("WebSocket connection established");

        session.subscribe("/topic/agreements", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                logger.debug("Received WebSocket frame: {}", new String((byte[]) payload));
                resultFuture.complete((byte[]) payload);
            }
        });
        logger.debug("Subscribed to /topic/agreements");

        String testMessage = "Test message";
        logger.debug("Sending test message: {}", testMessage);
        session.send("/app/agreements", testMessage.getBytes());

        byte[] result = resultFuture.get(3, TimeUnit.SECONDS);
        assertNotNull(result);
        assertEquals(testMessage, new String(result));
        logger.info("testWebSocketConnection completed successfully");
    }
}
