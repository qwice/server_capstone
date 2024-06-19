package com.example.capstone.repository;

import com.example.capstone.entity.PredictEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface PredictRepository extends JpaRepository<PredictEntity, Long> {
    List<PredictEntity> findByMemberMemberId(String memberId);
    List<PredictEntity> findByMemberMemberIdAndDate(String memberId, Date date);  // 필요에 따라 추가 변경
}
