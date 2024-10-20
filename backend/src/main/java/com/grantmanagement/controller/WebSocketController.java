package com.grantmanagement.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/agreement")
    @SendTo("/topic/agreements")
    public String handleAgreementUpdate(String message) {
        // Process the message and return an update
        return "Agreement updated: " + message;
    }
}
