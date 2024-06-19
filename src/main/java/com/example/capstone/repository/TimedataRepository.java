package com.example.capstone.repository;

import com.example.capstone.entity.TimedataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface TimedataRepository extends JpaRepository<TimedataEntity, Long> {
    TimedataEntity findByMemberMemberIdAndDate(String memberId, Date date);
    List<TimedataEntity> findByMemberMemberId(String memberId);
}
