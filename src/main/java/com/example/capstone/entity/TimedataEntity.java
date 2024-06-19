package com.example.capstone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.sql.Date;
import java.sql.Time;

@Entity
@Getter
@Setter
@Table(name = "Timedata", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"member_id", "date"})
})
public class TimedataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    @JsonBackReference // 추가된 부분
    private MemberEntity member;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = true)
    private Time exit_time;

    @Column(nullable = true)
    private Time entry_time;
}
