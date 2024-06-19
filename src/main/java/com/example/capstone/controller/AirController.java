package com.example.capstone.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;
import java.util.Map;

@RestController
@RequestMapping("/aircon")
public class AirController {

    //에어컨의 상태, 온도 받음
    @PostMapping("/on")
    public String turnOn() {
        sendCommandToRaspberryPi("on");
        return "aircon is turned ON";
    }

    @PostMapping("/off")
    public String turnOff() {
        sendCommandToRaspberryPi("off");
        return "aircon is turned OFF";
    }
    
    @PostMapping("/updateTemp")
    public String updateTemperature(@RequestBody Map<String, Integer> temperature) {
        int newTemp = temperature.get("temperature");
        System.out.println("Updating temperature to: " + newTemp);
        sendTemperatureToRaspberryPi(temperature);
        return "Temperature set to " + newTemp;
    }

    //라즈베리파이에 on/off 상태 보냄
    public String sendCommandToRaspberryPi(String command) {
        try {
            String url = "http://172.100.2.194:5000/aircon/" + command;
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.postForObject(url, null, String.class);
        } catch (ResourceAccessException e) {
            e.printStackTrace();
            return "Error: Unable to connect to Raspberry Pi";
        }
    }
    //라즈베리파이에 온도 보냄
    private String sendTemperatureToRaspberryPi(Map<String, Integer> data) {
        try {
            String url = "http://172.100.2.194:5000/aircon/updateTemp";
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.postForObject(url, data, String.class);
        } catch (ResourceAccessException e) {
            e.printStackTrace();
            return "Error: Unable to connect to Raspberry Pi";
        }
    }
}
