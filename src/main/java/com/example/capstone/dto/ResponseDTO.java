package com.example.capstone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {
    private boolean success;
    private String message;
    private MemberDTO data;
}
