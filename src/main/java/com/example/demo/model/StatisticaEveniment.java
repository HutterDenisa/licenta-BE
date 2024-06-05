package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "statisticaeveniment")

public class StatisticaEveniment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int act1;
    private int act2;
    private int act3;
    private int act4;
    private int act5;
    private int act6;
    private int act7;
    private int act8;
    private int act9;
    private int act10;
    private LocalDateTime t1;
    private LocalDateTime t2;
    private LocalDateTime t3;
    private LocalDateTime t4;
    private LocalDateTime t5;
    private LocalDateTime t6;
    private LocalDateTime t7;
    private LocalDateTime t8;
    private LocalDateTime t9;
    private LocalDateTime t10;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    public StatisticaEveniment(int act1, int act2, int act3, int act4, int act5, int act6, int act7, int act8, int act9, int act10, LocalDateTime t1, LocalDateTime t2, LocalDateTime t3, LocalDateTime t4, LocalDateTime t5, LocalDateTime t6, LocalDateTime t7, LocalDateTime t8, LocalDateTime t9, LocalDateTime t10, User user) {
        this.act1 = act1;
        this.act2 = act2;
        this.act3 = act3;
        this.act4 = act4;
        this.act5 = act5;
        this.act6 = act6;
        this.act7 = act7;
        this.act8 = act8;
        this.act9 = act9;
        this.act10 = act10;
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
        this.t5 = t5;
        this.t6 = t6;
        this.t7 = t7;
        this.t8 = t8;
        this.t9 = t9;
        this.t10 = t10;
        this.user = user;
    }

    public StatisticaEveniment() {

    }
}
