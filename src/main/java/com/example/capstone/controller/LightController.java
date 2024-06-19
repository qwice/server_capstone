package com.example.capstone.controller;

import com.example.capstone.entity.LightEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/lights")
@CrossOrigin(origins = "*")
public class LightController {

    private final String RASPBERRY_PI_API_URL = "http://172.100.2.194:2000/lights";

    private LightEntity lightEntity = new LightEntity("off");

    @PostMapping("/control")
    public ResponseEntity<String> controlLight(@RequestBody LightRequest lightRequest) {
        String status = lightRequest.getStatus();
        System.out.println("Received light status: " + status);

        // 상태를 업데이트
        lightEntity.setStatus(status);

        // 라즈베리파이 API 호출
        String result = callRaspberryPiAPI(status);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/status")
    public ResponseEntity<LightEntity> getLightStatus() {
        return ResponseEntity.ok(lightEntity);
    }

    private String callRaspberryPiAPI(String command) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestJson = String.format("{\"status\":\"%s\"}", command);
            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    RASPBERRY_PI_API_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error calling Raspberry Pi API: " + e.getMessage();
        }
    }

    static class LightRequest {
        private String status;

        // 기본 생성자
        public LightRequest() {
        }

        // 문자열을 받는 생성자
        @JsonCreator
        public LightRequest(@JsonProperty("status") String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
