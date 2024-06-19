package com.example.capstone.service;

import com.example.capstone.entity.MemberEntity;
import com.example.capstone.entity.TimedataEntity;
import com.example.capstone.repository.MemberRepository;
import com.example.capstone.repository.TimedataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class TimedataService {

    @Autowired
    private TimedataRepository timedataRepository;

    @Autowired
    private MemberRepository memberRepository;

    public TimedataEntity findOrCreateTimedata(String memberId, Date date) {
        TimedataEntity timedata = timedataRepository.findByMemberMemberIdAndDate(memberId, date);
        if (timedata == null) {
            timedata = new TimedataEntity();
            MemberEntity member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new IllegalArgumentException("No member found with ID: " + memberId));
            timedata.setMember(member);
            timedata.setDate(date);
        }
        return timedata;
    }

    public void saveTimedata(TimedataEntity timedata) {
        timedataRepository.save(timedata);
    }

    public List<TimedataEntity> findByMemberId(String memberId) {
        return timedataRepository.findByMemberMemberId(memberId);
    }

    public void saveAllTimedata(List<TimedataEntity> timedataList) {
        timedataRepository.saveAll(timedataList);
    }
}
