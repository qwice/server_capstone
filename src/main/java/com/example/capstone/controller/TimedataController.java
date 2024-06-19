package com.example.capstone.controller;

import com.example.capstone.dto.TimedataDTO;
import com.example.capstone.dto.MemberDTO; // 추가
import com.example.capstone.entity.TimedataEntity;
import com.example.capstone.entity.MemberEntity; // 추가
import com.example.capstone.service.TimedataService;
import com.example.capstone.service.MemberService; // 추가
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TimedataController {

    @Autowired
    private TimedataService timedataService;

    @Autowired
    private MemberService memberService; // 추가

    @PostMapping("/logTime")
    public ResponseEntity<?> logTime(@RequestBody TimedataDTO request) {
        try {
            System.out.println("Request received: " + request);
            TimedataEntity timedata = timedataService.findOrCreateTimedata(request.getMemberId(), request.getDate());
            if (request.getExitTime() != null) {
                timedata.setExit_time(request.getExitTime());
            }
            if (request.getEntryTime() != null) {
                timedata.setEntry_time(request.getEntryTime());
            }
            timedataService.saveTimedata(timedata);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/logTimes")
    public ResponseEntity<?> logTimes(@RequestBody List<TimedataDTO> requests) {
        try {
            List<TimedataEntity> timedataList = requests.stream().map(request -> {
                TimedataEntity timedata = timedataService.findOrCreateTimedata(request.getMemberId(), request.getDate());
                timedata.setExit_time(request.getExitTime());
                timedata.setEntry_time(request.getEntryTime());
                return timedata;
            }).collect(Collectors.toList());
            timedataService.saveAllTimedata(timedataList);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/getUserByMemberId/{memberId}")
    public ResponseEntity<?> getUserByMemberId(@PathVariable("memberId") String memberId) {
        try {
            Optional<MemberEntity> user = memberService.findByMemberId(memberId); // memberService를 사용하여 memberId로 찾기
            if (user.isPresent()) {
                MemberDTO userDTO = MemberDTO.toMemberDTO(user.get());
                return ResponseEntity.ok(userDTO);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/logTimes")
    public ResponseEntity<?> getLogTimesByMemberId(@RequestParam("memberId") String memberId) {
        try {
            List<TimedataEntity> timedataList = timedataService.findByMemberId(memberId);
            if (!timedataList.isEmpty()) {
                List<TimedataDTO> timedataDTOList = timedataList.stream().map(this::convertToDTO).collect(Collectors.toList());
                return ResponseEntity.ok(timedataDTOList);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "No data found for memberId " + memberId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private TimedataDTO convertToDTO(TimedataEntity timedataEntity) {
        TimedataDTO dto = new TimedataDTO();
        dto.setId(timedataEntity.getId());
        dto.setMemberId(timedataEntity.getMember().getMemberId());
        dto.setDate(timedataEntity.getDate());
        dto.setExitTime(timedataEntity.getExit_time());
        dto.setEntryTime(timedataEntity.getEntry_time());
        return dto;
    }
}
