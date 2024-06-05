package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "mesaj", schema = "public")
public class Mesaj {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;
    private String mesaj;
    private LocalDateTime t1;

    public Mesaj(User user, String mesaj, LocalDateTime t1) {
        this.user = user;
        this.mesaj = mesaj;
        this.t1 = t1;
    }

    public Mesaj() {
    }
}
