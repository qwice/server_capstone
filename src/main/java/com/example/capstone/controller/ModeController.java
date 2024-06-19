package com.example.capstone.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ModeController {

    private String controlMode = "manual";  // 초기 모드를 수동으로 설정

    @PostMapping("/setMode")
    public String setControlMode(@RequestBody Map<String, String> request) {
        String mode = request.get("mode");
        if (mode != null) {
            this.controlMode = mode;
            return "Mode updated to " + this.controlMode;
        } else {
            return "Mode not provided";
        }
    }

    @GetMapping("/getMode")
    public String getControlMode() {
        return this.controlMode;
    }
}
