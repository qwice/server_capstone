package com.example.capstone.service;

import com.example.capstone.entity.PredictEntity;
import com.example.capstone.repository.PredictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PredictService {
    @Autowired
    private PredictRepository predictRepository;

    public void savePredictedTimes(PredictEntity predictEntity) {
        predictRepository.save(predictEntity);
    }

    public List<PredictEntity> getPredictedTimesByMemberId(String memberId) {
        return predictRepository.findByMemberMemberId(memberId);
    }

    public List<PredictEntity> getAllPredictions() {
        return predictRepository.findAll();
    }

    public PredictEntity updatePredictedTimes(PredictEntity predictEntity) {
        List<PredictEntity> existingEntities = predictRepository.findByMemberMemberIdAndDate(predictEntity.getMember().getMemberId(), predictEntity.getDate());
        if (!existingEntities.isEmpty()) {
            for (PredictEntity entity : existingEntities) {
                if (entity.getType().equals(predictEntity.getType())) {
                    entity.setTime(predictEntity.getTime());
                    return predictRepository.save(entity);
                }
            }
        }
        return predictRepository.save(predictEntity);
    }
}
