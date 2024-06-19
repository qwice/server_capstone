package com.example.capstone.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "predict", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"member_id", "date", "type"})
})
public class PredictEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private MemberEntity member;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date predictionDate = new Date();
}

