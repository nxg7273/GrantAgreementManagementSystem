package com.grantmanagement.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/agreements")
    @SendTo("/topic/agreements")
    public String handleAgreementUpdate(String message) {
        // Simply echo the message back without modification
        return message;
    }
}
