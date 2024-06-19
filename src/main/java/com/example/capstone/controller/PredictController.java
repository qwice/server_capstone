package com.example.capstone.controller;

import com.example.capstone.entity.PredictEntity;
import com.example.capstone.dto.MemberDTO;
import com.example.capstone.entity.MemberEntity;
import com.example.capstone.service.PredictService;
import com.example.capstone.service.MemberService; // MemberService 추가
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PredictController {
    private static final Logger logger = LoggerFactory.getLogger(PredictController.class);

    @Autowired
    private PredictService predictService;

    @Autowired
    private MemberService memberService; // MemberService를 추가

    @Autowired
    private LightController lightController;

    @Autowired
    private AirController airController;

    @Autowired
    private BoilerController boilerController;

    @Autowired
    private ModeController modeController;

    private Map<String, Integer> autoControlTemperatures = new HashMap<>();  // 자동 제어 모드의 온도 저장


    @PostMapping("/savePrediction")
    public ResponseEntity<String> savePrediction(@RequestBody PredictEntity predictedTimesEntity) {
        try {
            // 멤버 ID를 통해 기존 멤버를 검색
            Optional<MemberEntity> memberOptional = memberService.findByMemberId(predictedTimesEntity.getMember().getMemberId());
            MemberEntity member = memberOptional.orElseGet(() -> {
                // 멤버가 존재하지 않으면 새로 생성
                MemberEntity newMember = new MemberEntity();
                newMember.setMemberId(predictedTimesEntity.getMember().getMemberId());
                newMember.setMemberName(predictedTimesEntity.getMember().getMemberName()); // 예시: 이름 설정 필요
                newMember.setMemberPassword("defaultPassword"); // 예시: 기본 비밀번호 설정

                // MemberDTO로 변환
                MemberDTO memberDTO = MemberDTO.toMemberDTO(newMember);
                memberService.save(memberDTO); // DTO를 이용하여 저장
                return newMember;
            });

            predictedTimesEntity.setMember(member);
            predictService.savePredictedTimes(predictedTimesEntity);
            return ResponseEntity.ok("Predicted times saved successfully");
        } catch (Exception e) {
            logger.error("Error saving predicted times", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/updatePrediction")
    public ResponseEntity<String> updatePrediction(@RequestBody PredictEntity predictEntity) {
        try {
            // 멤버 존재 유무 확인
            MemberEntity member = memberService.findByMemberId(predictEntity.getMember().getMemberId())
                    .orElseThrow(() -> new IllegalStateException("Member not found: " + predictEntity.getMember().getMemberId()));

            predictEntity.setMember(member);

            // 예측 업데이트 로직
            PredictEntity updatedEntity = predictService.updatePredictedTimes(predictEntity);
            if (updatedEntity != null) {
                return ResponseEntity.ok("Prediction updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No suitable existing prediction found to update");
            }
        } catch (Exception e) {
            logger.error("Error updating prediction", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating prediction: " + e.getMessage());
        }
    }

    @PostMapping("/setAutoControlTemperature")
    public ResponseEntity<String> setAutoControlTemperature(@RequestBody Map<String, Integer> temperatures) {
        autoControlTemperatures.putAll(temperatures);
        return ResponseEntity.ok("Auto control temperatures updated");
    }

    @GetMapping("/getPredictedTimesByMemberId")
    public ResponseEntity<?> getPredictedTimesByMemberId(@RequestParam(name = "memberId") String memberId) {
        try {
            List<PredictEntity> predictedTimes = predictService.getPredictedTimesByMemberId(memberId);
            if (predictedTimes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No predictions found for memberId " + memberId);
            }
            return ResponseEntity.ok(predictedTimes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching predicted times: " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 * * * * ?")
    public void controlDevicesAtPredictedTime() {
        logger.info("Scheduled task running...");
        String currentMode = modeController.getControlMode();
        if (!"자동 제어 모드".equals(currentMode)) {
            logger.info("Current mode is not 자동 제어 모드. Exiting...");
            return;
        }

        List<PredictEntity> predictions = predictService.getAllPredictions();
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (PredictEntity prediction : predictions) {
            if (now.format(formatter).equals(prediction.getTime())) {
                logger.info("Match found for prediction time: {}", prediction.getTime());
                switch (prediction.getType()) {
                    case "exit_time":
                        logger.info("Executing exit_time actions");
                        lightController.controlLight(new LightController.LightRequest("off"));
                        airController.turnOff();
                        boilerController.turnOff();
                        break;
                    case "entry_time":
                        logger.info("Executing entry_time actions");
                        lightController.controlLight(new LightController.LightRequest("on"));
                        airController.turnOn();
                        airController.updateTemperature(Map.of("temperature", autoControlTemperatures.getOrDefault("aircon", 21)));
                        boilerController.turnOn();
                        boilerController.updateTemperature(Map.of("temperature", autoControlTemperatures.getOrDefault("boiler", 24)));
                        break;
                }
            }
        }
    }
}