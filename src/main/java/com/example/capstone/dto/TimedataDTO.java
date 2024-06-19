package com.example.capstone.dto;

import java.sql.Date;
import java.sql.Time;

public class TimedataDTO {
    private Long id;
    private String memberId; // 변경: Long -> String
    private Date date;
    private Time entryTime;
    private Time exitTime;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) { 
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Time entryTime) {
        this.entryTime = entryTime;
    }

    public Time getExitTime() {
        return exitTime;
    }

    public void setExitTime(Time exitTime) {
        this.exitTime = exitTime;
    }
}
