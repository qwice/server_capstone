package com.example.capstone.controller;

import com.example.capstone.entity.TemperatureEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class TemperatureController {

    private TemperatureEntity latestTemperature = new TemperatureEntity(); // 가장 최근의 온도 및 습도 정보를 저장할 변수

    @PostMapping("/temperature")
    public void receiveTemperature(@RequestBody TemperatureEntity temperatureEntity) {
        // POST 매핑으로 온도 및 습도 정보를 받아서 latestTemperature에 저장합니다.
        latestTemperature.setTemperature(temperatureEntity.getTemperature());
        latestTemperature.setHumidity(temperatureEntity.getHumidity());
        System.out.println("Temperature received: " + temperatureEntity.getTemperature());
        System.out.println("Humidity received: " + temperatureEntity.getHumidity());
    }

    @GetMapping("/temperature")
    public TemperatureEntity getTemperature() {
        // 가장 최근의 온도 및 습도 정보를 반환합니다.
        return latestTemperature;
    }
}