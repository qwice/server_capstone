package com.example.capstone.controller;

import com.example.capstone.entity.WindowEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/windows")
@CrossOrigin(origins = "*")
public class WindowController {

    private final String RASPBERRY_PI_API_URL = "http://172.100.2.194:9000/windows";

    private WindowEntity windowEntity = new WindowEntity("off");

    @PostMapping("/control")
    public ResponseEntity<String> controlWindow(@RequestBody WindowStatusRequest windowStatusRequest) {
        String status = windowStatusRequest.getStatus();
        System.out.println("Received window status: " + status);

        // 상태를 업데이트
        windowEntity.setStatus(status);

        // 라즈베리파이 API 호출
        String result = callRaspberryPiAPI(status);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/status")
    public ResponseEntity<WindowEntity> getWindowStatus() {
        return ResponseEntity.ok(windowEntity);
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

    public static class WindowStatusRequest {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
