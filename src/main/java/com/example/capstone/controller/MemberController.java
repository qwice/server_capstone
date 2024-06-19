package com.example.capstone.controller;

import com.example.capstone.dto.MemberDTO;
import com.example.capstone.dto.ResponseDTO;
import com.example.capstone.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @GetMapping("/member/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("MemberController.save");
        System.out.println("memberDTO = " + memberDTO);
        memberService.save(memberDTO);
        return "login";
    }

    @PostMapping("/member/savel")
    public ResponseDTO savel(@RequestBody MemberDTO memberDTO) {
        System.out.println("MemberController.save");
        System.out.println("memberDTO = " + memberDTO);
        memberService.save(memberDTO);
        return new ResponseDTO(true, "Signup successful", memberDTO);
    }

    @GetMapping("/member/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {

        MemberDTO loginResult = memberService.login(memberDTO);
        if(loginResult != null) {
            //login 성공
            session.setAttribute("loginId", loginResult.getMemberId()); // 로그인한 회원 정보의 아이디를 session에다 담아줌
            return "main";
        } else{
            //login 실패
            return "login";
        }
    }

    @PostMapping("member/loginl")
    public ResponseDTO loginl(@RequestBody MemberDTO memberDTO, HttpSession session) {
        System.out.println("memberDTO = " + memberDTO);
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            // login 성공
            session.setAttribute("loginId", loginResult.getMemberId()); // 로그인한 회원 정보의 아이디를 session에다 담아줌
            return new ResponseDTO(true, "Login successful", loginResult);
        } else {
            // login 실패
            return new ResponseDTO(false, "Login failed, please check your username and password", null);
        }
    }
}
