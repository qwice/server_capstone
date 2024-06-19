package com.example.capstone.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/audio")
public class TextController {

    @Autowired
    private AirController airController;
    @Autowired
    private BoilerController boilerController;
    @Autowired
    private LightController lightController;
    @Autowired
    private WindowController windowController;

    @PostMapping("/text")
    public ResponseEntity<String> receiveText(@RequestBody Request request) {
        String receivedText = request.getText();
        System.out.println("Received text: " + receivedText);

        if (receivedText.contains("불")) {
            if (receivedText.contains("켜")) {
                LightController.LightRequest lightRequest = new LightController.LightRequest();
                lightRequest.setStatus("on");
                return lightController.controlLight(lightRequest);
            } else if (receivedText.contains("꺼")) {
                LightController.LightRequest lightRequest = new LightController.LightRequest();
                lightRequest.setStatus("off");
                return lightController.controlLight(lightRequest);
            }
        }
        if (receivedText.contains("창문")) {
            if (receivedText.contains("열어")) {
                WindowController.WindowStatusRequest windowStatusRequest = new WindowController.WindowStatusRequest();
                windowStatusRequest.setStatus("on");
                return windowController.controlWindow(windowStatusRequest);
            } else if (receivedText.contains("닫아")) {
                WindowController.WindowStatusRequest windowStatusRequest = new WindowController.WindowStatusRequest();
                windowStatusRequest.setStatus("off");
                return windowController.controlWindow(windowStatusRequest);
            }
        }
        if (receivedText.contains("에어컨")) {
            if (receivedText.contains("켜")) {
                return ResponseEntity.ok(airController.turnOn());
            } else if (receivedText.contains("꺼")) {
                return ResponseEntity.ok(airController.turnOff());
            }
        }
        if (receivedText.contains("보일러")) {
            if (receivedText.contains("켜")) {
                return ResponseEntity.ok(boilerController.turnOn());
            } else if (receivedText.contains("꺼")) {
                return ResponseEntity.ok(boilerController.turnOff());
            }
        }
        return ResponseEntity.ok("명령을 이해하지 못했습니다.");
    }
}
