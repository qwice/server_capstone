package com.example.capstone.service;

import com.example.capstone.dto.MemberDTO;
import com.example.capstone.entity.MemberEntity;
import com.example.capstone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public void save(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    public MemberDTO login(MemberDTO memberDTO) {
        Optional<MemberEntity> byMemberId = memberRepository.findByMemberId(memberDTO.getMemberId());
        if(byMemberId.isPresent()) {
            // 조회 결과가 있다(해당 아이디를 가진 회원 정보가 있다)
            MemberEntity memberEntity = byMemberId.get();
            if(memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                // 비밀번호 일치
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;
            } else {
                // 비밀번호 불일치
                return null;
            }
        } else {
            // 조회 결과가 없다(해당 아이디를 가진 회원 정보가 없다)
            return null;
        }
    }
    public Optional<MemberEntity> findByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId);
    }
}
